package com.genAI.microservice.paymentgateway_service.service;

import com.genAI.microservice.paymentgateway_service.model.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class CurrencyExchangeService {

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable(value = "exchangeRates", key = "#fromCurrency.concat('-').concat(#toCurrency)")
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        String url = "https://api.exchangeratesapi.io/latest?base=" + fromCurrency + "&symbols=" + toCurrency;
        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);
        return response != null ? response.getRates().get(toCurrency) : null;
    }
}
