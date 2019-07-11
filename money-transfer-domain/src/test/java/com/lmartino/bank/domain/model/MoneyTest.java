package com.lmartino.bank.domain.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class MoneyTest {

    private Currency eur = Currency.of("EUR");

    @Test
    public void canCreateAmountValueObject(){
        final BigDecimal money = BigDecimal.valueOf(12.99);
        Money amount = Money.of(money, eur);
        Assert.assertThat(amount, is(notNullValue()));
        Assert.assertThat(amount.getValue(), is(money));
    }

    @Test
    public void canDecreaseAmount(){
        BigDecimal initialMoney = BigDecimal.valueOf(12.99);
        Money money = Money.of(initialMoney, eur);
        BigDecimal moneyToDecrease = BigDecimal.valueOf(9.08);
        Money amountToDecrease = Money.of(moneyToDecrease, eur);
        money.decreaseBy(amountToDecrease);
        Assert.assertThat(money.getValue(), is(initialMoney.subtract(moneyToDecrease)));
    }

    @Test
    public void canIncreaseAmount(){
        BigDecimal initialMoney = BigDecimal.valueOf(12.99);
        Money money = Money.of(initialMoney,eur );
        BigDecimal moneyToIncrease = BigDecimal.valueOf(9.08);
        Money amountToIncrease = Money.of(moneyToIncrease, eur);
        money.increaseBy(amountToIncrease);
        Assert.assertThat(money.getValue(), is(initialMoney.add(moneyToIncrease)));
    }

    @Test
    public void canCheckIfAmountIsGreaterThanAnotherAmount(){
        Money greaterMoney = Money.of(BigDecimal.valueOf(12.99), eur);
        Money lowerMoney = Money.of(BigDecimal.valueOf(9.08), eur);
        Assert.assertThat(greaterMoney.isGreaterThan(lowerMoney), is(true));
    }

    @Test
    public void canApplyIdentityRateOnAmount(){
        BigDecimal value = BigDecimal.valueOf(12.99);
        Money money = Money.of(value, eur);
        Assert.assertThat(money.applyRate(BigDecimal.ONE, eur).getValue(), is(value));
    }

    @Test
    public void canApplyRateOnAmount(){
        BigDecimal value = BigDecimal.ONE;
        Money money = Money.of(value, eur);
        Assert.assertThat(money.applyRate(BigDecimal.valueOf(0.89919), eur).getValue().doubleValue(), is(0.90));
    }

}