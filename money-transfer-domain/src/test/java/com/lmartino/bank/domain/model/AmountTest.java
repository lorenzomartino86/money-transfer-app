package com.lmartino.bank.domain.model;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class AmountTest {

    @Test
    public void canCreateAmountValueObject(){
        final BigDecimal money = BigDecimal.valueOf(12.99);
        Amount amount = Amount.of(money);
        Assert.assertThat(amount, is(notNullValue()));
        Assert.assertThat(amount.getMoney(), is(money));
    }

    @Test
    public void canDecreaseAmount(){
        BigDecimal initialMoney = BigDecimal.valueOf(12.99);
        Amount amount = Amount.of(initialMoney);
        BigDecimal moneyToDecrease = BigDecimal.valueOf(9.08);
        Amount amountToDecrease = Amount.of(moneyToDecrease);
        amount.decreaseBy(amountToDecrease);
        Assert.assertThat(amount.getMoney(), is(initialMoney.subtract(moneyToDecrease)));
    }

    @Test
    public void canIncreaseAmount(){
        BigDecimal initialMoney = BigDecimal.valueOf(12.99);
        Amount amount = Amount.of(initialMoney);
        BigDecimal moneyToIncrease = BigDecimal.valueOf(9.08);
        Amount amountToIncrease = Amount.of(moneyToIncrease);
        amount.increaseBy(amountToIncrease);
        Assert.assertThat(amount.getMoney(), is(initialMoney.add(moneyToIncrease)));
    }

    @Test
    public void canCheckIfAmountIsGreaterThanAnotherAmount(){
        Amount greaterAmount = Amount.of(BigDecimal.valueOf(12.99));
        Amount lowerAmount = Amount.of(BigDecimal.valueOf(9.08));
        Assert.assertThat(greaterAmount.isGreaterThan(lowerAmount), is(true));
    }

}