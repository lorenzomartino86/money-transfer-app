package com.lmartino.bank.domain.model;

import com.lmartino.bank.domain.exception.InsufficientBalanceException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class AccountTest {

    @Test
    public void canCreateNewAccount(){
        String name = "Account Name";
        Amount balance = Amount.of(BigDecimal.valueOf(1500.0));
        Account account = Account.createNewAccount(name, balance);
        Assert.assertThat(account, is(notNullValue()));
        Assert.assertThat(account.getName(), is(name));
        Assert.assertThat(account.getBalance(), is(balance));
        Assert.assertThat(account.getId(), is(notNullValue()));
    }

    @Test
    public void givenOverallHealthyBalance_canWithdrawAmountFromAccount(){
        BigDecimal initialBalance = BigDecimal.valueOf(1500.0);
        Account account = Account.createNewAccount("Account Name", Amount.of(initialBalance));
        BigDecimal withdrawAmount = BigDecimal.valueOf(123.98);
        account.withdraw(Amount.of(withdrawAmount));
        Assert.assertThat(account, is(notNullValue()));
        Assert.assertThat(account.getName(), is("Account Name"));
        Assert.assertThat(account.getBalance().getMoney(), is(initialBalance.subtract(withdrawAmount)));
    }

    @Test(expected = InsufficientBalanceException.class)
    public void givenUnhealthyBalance_cannotWithdrawAmountFromAccount(){
        BigDecimal initialBalance = BigDecimal.valueOf(120.1);
        Account account = Account.createNewAccount("Account Name", Amount.of(initialBalance));
        BigDecimal withdrawAmount = BigDecimal.valueOf(123.98);
        account.withdraw(Amount.of(withdrawAmount));
    }


    @Test
    public void canDepositAmountIntoAccount(){
        BigDecimal initialBalance = BigDecimal.valueOf(1500.0);
        Account account = Account.createNewAccount("Account Name", Amount.of(initialBalance));
        BigDecimal depositAmount = BigDecimal.valueOf(123.98);
        account.deposit(Amount.of(depositAmount));
        Assert.assertThat(account, is(notNullValue()));
        Assert.assertThat(account.getName(), is("Account Name"));
        Assert.assertThat(account.getBalance().getMoney(), is(initialBalance.add(depositAmount)));
    }

}
