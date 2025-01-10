package com.genAI.microservice.paymentgateway_service.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Setter
@Getter
public class ExchangeRateResponse {

    private Map<String, BigDecimal> rates;

}
