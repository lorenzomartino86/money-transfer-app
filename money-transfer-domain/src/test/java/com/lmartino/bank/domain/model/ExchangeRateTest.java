package com.lmartino.bank.domain.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ExchangeRateTest {
    private Currency from = Currency.of("EUR");
    private Currency to = Currency.of("USD");
    private double value = 1.12;

    @Test
    public void canCreateExchangeRate(){
        ExchangeRate rate = ExchangeRate.create(from, to, BigDecimal.valueOf(value));
        Assert.assertThat(rate, is(notNullValue()));
        Assert.assertThat(rate.getId(), is(notNullValue()));
        Assert.assertThat(rate.getRate().doubleValue(), is(value));
        Assert.assertThat(rate.getFromCurrency(), is(from));
        Assert.assertThat(rate.getToCurrency(), is(to));
    }

    @Test
    public void canConsumeFactoryMethod(){
        ExchangeRate rate = ExchangeRate.of("123", from, to, BigDecimal.valueOf(value));
        Assert.assertThat(rate, is(notNullValue()));
        Assert.assertThat(rate.getId(), is(Id.of("123")));
        Assert.assertThat(rate.getRate().doubleValue(), is(value));
        Assert.assertThat(rate.getFromCurrency(), is(from));
        Assert.assertThat(rate.getToCurrency(), is(to));
    }


}