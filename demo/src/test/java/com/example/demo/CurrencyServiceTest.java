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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void testGetCurrencyValue_ValidCurrency_ReturnsValue() {
        // Mockowanie odpowiedzi z NBP API
        Map<String, Object> rate = Map.of("code", "EUR", "mid", 4.3607);
        Map<String, Object> response = Map.of("rates", List.of(rate));

        lenient().when(restTemplate.getForObject(NBP_API_URL, Map[].class))
                .thenReturn(new Map[]{response});

        // Testowanie poprawnego zwrócenia wartości waluty
        double result = currencyService.getCurrencyValue("EUR");
        assertEquals(4.3607, result, "Powinien zwrócić kurs waluty EUR równy 4.3607");
    }

    @Test
    public void testGetCurrencyValue_InvalidCurrency_ThrowsException() {
        // Mockowanie odpowiedzi z NBP API z pustą listą walut
        Map<String, Object> response = Map.of("rates", List.of());

        lenient().when(restTemplate.getForObject(NBP_API_URL, Map[].class))
                .thenReturn(new Map[]{response});

        // Testowanie wyjątku przy nieistniejącej walucie
        assertThrows(ResponseStatusException.class, () -> {
            currencyService.getCurrencyValue("XYZ");
        }, "Powinien zgłosić wyjątek ResponseStatusException dla nieistniejącej waluty");
    }

    @Test
    public void testSaveRequest_ValidCurrency_SavesRequest() {
        // Mockowanie odpowiedzi z NBP API
        Map<String, Object> rate = Map.of("code", "EUR", "mid", 4.3607);
        Map<String, Object> response = Map.of("rates", List.of(rate));

        lenient().when(restTemplate.getForObject(NBP_API_URL, Map[].class))
                .thenReturn(new Map[]{response});

        // Mockowanie zapisu do repozytorium
        CurrencyRequest currencyRequest = new CurrencyRequest("EUR", "Jan Nowak", 4.3607);
        when(repository.save(any(CurrencyRequest.class))).thenReturn(currencyRequest);

        // Testowanie metody zapisu
        CurrencyRequest savedRequest = currencyService.saveRequest("EUR", "Jan Nowak");

        assertNotNull(savedRequest, "Zapisane żądanie nie powinno być null");
        assertEquals("EUR", savedRequest.getCurrency(), "Kod waluty powinien być równy EUR");
        assertEquals("Jan Nowak", savedRequest.getName(), "Imię użytkownika powinno być Jan Nowak");
        assertEquals(4.3607, savedRequest.getCurrencyValue(), "Wartość kursu powinna wynosić 4.3607");

        verify(repository, times(1)).save(any(CurrencyRequest.class));
    }

}
