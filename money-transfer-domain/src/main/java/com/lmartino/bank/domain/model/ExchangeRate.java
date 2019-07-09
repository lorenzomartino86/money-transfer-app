package com.lmartino.bank.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ExchangeRate {
    private Id id;
    private Currency fromCurrency;
    private Currency toCurrency;
    private Amount rate;

    private ExchangeRate(Id id, Currency fromCurrency, Currency toCurrency, Amount rate) {
        this.id = id;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
    }

    public static ExchangeRate of(final String id, final Currency from, final Currency to, final Amount rate){
        return new ExchangeRate(Id.of(id), from, to, rate);
    }

    public static ExchangeRate create(final Currency from, final Currency to, final Amount rate){
        return new ExchangeRate(Id.create(), from, to, rate);
    }

}
