package com.lmartino.bank.domain.model;

import lombok.Getter;

@Getter
public class ExchangeRate {
    private Currency fromCurrency;
    private Currency toCurrency;
    private Amount rate;

    private ExchangeRate(Currency fromCurrency, Currency toCurrency, Amount rate) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
    }

    public static ExchangeRate of(final Currency from, final Currency to, final Amount rate){
        return new ExchangeRate(from, to, rate);
    }
}
