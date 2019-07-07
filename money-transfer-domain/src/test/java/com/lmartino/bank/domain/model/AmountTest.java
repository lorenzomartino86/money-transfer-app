package com.lmartino.bank.domain.model;

import com.lmartino.bank.domain.model.Amount;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class AmountTest {

    @Test
    public void canCreateAmountValueObject(){
        final double money = 12.99;
        Amount amount = Amount.of(money);
        Assert.assertThat(amount, is(notNullValue()));
        Assert.assertThat(amount.getMoney(), is(money));
    }

    @Test
    public void canDecreaseAmount(){
        double initialMoney = 12.99;
        Amount amount = Amount.of(initialMoney);
        double moneyToDecrease = 9.08;
        Amount amountToDecrease = Amount.of(moneyToDecrease);
        amount.decreaseBy(amountToDecrease);
        Assert.assertThat(amount.getMoney(), is(initialMoney - moneyToDecrease));
    }

    @Test
    public void canIncreaseAmount(){
        double initialMoney = 12.99;
        Amount amount = Amount.of(initialMoney);
        double moneyToIncrease = 9.08;
        Amount amountToIncrease = Amount.of(moneyToIncrease);
        amount.increaseBy(amountToIncrease);
        Assert.assertThat(amount.getMoney(), is(initialMoney + moneyToIncrease));
    }

    @Test
    public void canCheckIfAmountIsGreaterThanAnotherAmount(){
        Amount greaterAmount = Amount.of(12.99);
        Amount lowerAmount = Amount.of(9.08);
        Assert.assertThat(greaterAmount.isGreaterThan(lowerAmount), is(true));
    }

}