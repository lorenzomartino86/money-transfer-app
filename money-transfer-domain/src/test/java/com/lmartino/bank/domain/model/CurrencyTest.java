package com.lmartino.bank.domain.model;

import com.lmartino.bank.domain.exception.UnknownCurrencyCodeException;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CurrencyTest {

    @Test
    public void allowedCurrencyAreProcessable() {
        Currency currency = Currency.of("EUR");
        Assert.assertThat(currency, is(notNullValue()));
    }

    @Test(expected = UnknownCurrencyCodeException.class)
    public void notAllowedCurrencyAreForbidden() {
        Currency.of("AUD");
    }

    @Test
    public void canCheckIfCurrenciesAreTheSame() {
        Assert.assertTrue(Currency.of("GBP").equals(Currency.of("GBP")));
    }

    @Test
    public void canCheckIfCurrenciesAreDifferent() {
        Assert.assertFalse(Currency.of("GBP").equals(Currency.of("EUR")));
    }

}