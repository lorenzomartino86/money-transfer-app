package com.lmartino.bank.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ExchangeRate {
    private Id id;
    private Currency fromCurrency;
    private Currency toCurrency;
    private BigDecimal rate;

    private ExchangeRate(Id id, Currency fromCurrency, Currency toCurrency, BigDecimal rate) {
        this.id = id;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
    }

    public static ExchangeRate of(final String id, final Currency from, final Currency to, final BigDecimal rate){
        return new ExchangeRate(Id.of(id), from, to, rate);
    }

    public static ExchangeRate create(final Currency from, final Currency to, final BigDecimal rate){
        return new ExchangeRate(Id.create(), from, to, rate);
    }

}
