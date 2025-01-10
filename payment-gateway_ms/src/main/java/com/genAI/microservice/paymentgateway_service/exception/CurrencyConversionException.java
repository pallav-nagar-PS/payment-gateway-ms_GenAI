package com.genAI.microservice.paymentgateway_service.exception;

public class CurrencyConversionException extends RuntimeException {
    public CurrencyConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
