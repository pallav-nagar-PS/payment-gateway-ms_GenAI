package com.genAI.microservice.paymentgateway_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Profile("dev")
public class CurrencyConverter {
    private final RestTemplate restTemplate;
    private final String apiBaseUrl;
    private final String apiKey;

    @Autowired
    public CurrencyConverter(RestTemplate restTemplate,
                             @Value("${currency.api.baseurl}") String apiBaseUrl,
                             @Value("${currency.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
        this.apiKey = apiKey;
    }

    @Cacheable(value = "currencyRates", key = "#fromCurrency.concat('-').concat(#toCurrency)")
    @Retryable(exceptionExpression= "{CurrencyConversionException.class}", maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public BigDecimal convert(String fromCurrency, String toCurrency, BigDecimal amount) {
        String url = String.format("%s/latest?access_key=%s&base=%s&symbols=%s",
                apiBaseUrl, apiKey, fromCurrency, toCurrency);
        CurrencyExchangeService response = restTemplate.getForObject(url, CurrencyExchangeService.class);
        assert response != null;
        Optional<BigDecimal> exchangeRate = Optional.ofNullable(response.getExchangeRate(fromCurrency, toCurrency));
        return amount.multiply(exchangeRate.orElse(null));
    }
}
