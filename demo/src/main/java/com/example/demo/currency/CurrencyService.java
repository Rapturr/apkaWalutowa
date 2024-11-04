package com.example.demo.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRequestRepository repository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String NBP_API_URL = "http://api.nbp.pl/api/exchangerates/tables/A?format=json";

    public double getCurrencyValue(String currency) {
        try {
            Map<String, Object>[] response = restTemplate.getForObject(NBP_API_URL, Map[].class);
            List<Map<String, Object>> rates = (List<Map<String, Object>>) response[0].get("rates");
            return rates.stream()
                    .filter(rate -> rate.get("code").equals(currency))
                    .map(rate -> (double) rate.get("mid"))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving currency data");
        }
    }

    public CurrencyRequest saveRequest(String currency, String name) {
        double value = getCurrencyValue(currency);
        CurrencyRequest request = new CurrencyRequest(currency, name, value); // Przekazujemy "value" do "currencyValue"
        return repository.save(request);
    }

    public List<CurrencyRequest> getAllRequests() {
        return repository.findAll();
    }

}
