package com.example.demo;

import com.example.demo.currency.CurrencyRequest;
import com.example.demo.currency.CurrencyRequestRepository;
import com.example.demo.currency.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    private final String NBP_API_URL = "http://api.nbp.pl/api/exchangerates/tables/A?format=json";

    @Mock
    private CurrencyRequestRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRequests() {
        // Przygotowanie mockowanych danych dla testu
        List<CurrencyRequest> mockRequests = List.of(
                new CurrencyRequest("EUR", "Anna Nowak", 3.54),
                new CurrencyRequest("USD", "Marek Kowalski", 3.12)
        );
        when(repository.findAll()).thenReturn(mockRequests);

        // Wywołanie metody i sprawdzenie wyników
        List<CurrencyRequest> allRequests = currencyService.getAllRequests();
        assertEquals(2, allRequests.size(), "Rozmiar listy zapytań powinien wynosić 2");
        assertEquals("EUR", allRequests.get(0).getCurrency());
        assertEquals("Anna Nowak", allRequests.get(0).getName());
        assertEquals(3.54, allRequests.get(0).getCurrencyValue(), "Kurs dla waluty EUR powinien być 3.54");
        assertEquals("USD", allRequests.get(1).getCurrency());
        assertEquals("Marek Kowalski", allRequests.get(1).getName());
        assertEquals(3.12, allRequests.get(1).getCurrencyValue(), "Kurs dla waluty USD powinien być 3.12");

        // Weryfikacja, że findAll() zostało wywołane
        verify(repository, times(1)).findAll();
    }



}
