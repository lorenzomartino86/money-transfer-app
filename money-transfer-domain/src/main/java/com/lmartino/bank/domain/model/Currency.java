package com.lmartino.bank.domain.model;

import lombok.Getter;

@Getter
public class Currency {
    private String value;

    private Currency(String value) {
        this.value = value;
    }

    public static Currency of(final String value){
        return new Currency(value);
    }
}
