package com.example.demo.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/currencies")
public class CurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @PostMapping("/get-current-currency-value-command")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Double> getCurrentCurrencyValue(@RequestBody Map<String, String> request) {
        String currency = request.get("currency");
        String name = request.get("name");
        CurrencyRequest savedRequest = currencyService.saveRequest(currency, name);
        return Map.of("value", savedRequest.getCurrencyValue());
    }

    @GetMapping("/requests")
    public List<CurrencyRequest> getAllRequests() {
        return currencyService.getAllRequests();
    }
}
