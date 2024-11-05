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

    public double getCurrencyValue(String currencyCode) {
        String url = "http://api.nbp.pl/api/exchangerates/tables/A?format=json";

        Map<String, Object>[] response = restTemplate.getForObject(url, Map[].class);

        if (response != null && response.length > 0) {
            List<Map<String, Object>> rates = (List<Map<String, Object>>) response[0].get("rates");

            for (Map<String, Object> rate : rates) {
                if (currencyCode.equals(rate.get("code"))) {
                    return (Double) rate.get("mid");
                }
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Waluta " + currencyCode + " nie została znaleziona");
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
