package com.lmartino.bank.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

import static com.lmartino.bank.domain.exception.DomainExceptionHandler.unknownCurrencyCodeException;

@Getter
@ToString
@EqualsAndHashCode
public class Currency {
    private String value;

    private static List<String> validCurrencies = Arrays.asList("EUR", "GBP", "USD");

    private Currency(String value) {
        this.value = value;
    }

    public static Currency of(final String value){
        if (!validCurrencies.contains(value)) unknownCurrencyCodeException(value);
        return new Currency(value);
    }
}
