package com.example.demo.currency;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "currency_requests")
public class CurrencyRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currency;
    private String name;
    private LocalDateTime requestDate;

    @Column(name = "currency_value")
    private double currencyValue;

    public CurrencyRequest() {}

    public CurrencyRequest(String currency, String name, double currencyValue) {
        this.currency = currency;
        this.name = name;
        this.requestDate = LocalDateTime.now();
        this.currencyValue = currencyValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public double getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(double currencyValue) {
        this.currencyValue = currencyValue;
    }
}