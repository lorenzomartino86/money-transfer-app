package com.lmartino.bank.domain.model;

import java.util.List;

import static com.lmartino.bank.domain.exception.DomainExceptionHandler.unknownCurrencyCodeException;

public class AllowedCurrencies {
    private final List<String> currencies;

    public AllowedCurrencies(List<String> currencies) {
        this.currencies = currencies;
    }

    public void isAllowed(final String currency){
        if (!currencies.contains(currency)){
            unknownCurrencyCodeException(currency);
        }
    }
}
