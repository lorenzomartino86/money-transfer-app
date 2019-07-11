package com.lmartino.bank.domain.model;

import com.lmartino.bank.domain.exception.InsufficientBalanceException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class AccountTest {

    private Currency gbp = Currency.of("GBP");;

    @Test
    public void canCreateNewAccount(){
        String name = "Account Name";
        Money balance = Money.of(BigDecimal.valueOf(1500.0), gbp);
        Account account = Account.createNewAccount(name, balance);
        Assert.assertThat(account, is(notNullValue()));
        Assert.assertThat(account.getName(), is(name));
        Assert.assertThat(account.getBalance(), is(balance));
        Assert.assertThat(account.getId(), is(notNullValue()));
    }

    @Test
    public void givenOverallHealthyBalance_canWithdrawAmountFromAccount(){
        BigDecimal initialBalance = BigDecimal.valueOf(1500.0);
        Account account = Account.createNewAccount("Account Name", Money.of(initialBalance, gbp));
        BigDecimal withdrawAmount = BigDecimal.valueOf(123.98);
        account.withdraw(Money.of(withdrawAmount, Currency.of("GBP")));
        Assert.assertThat(account, is(notNullValue()));
        Assert.assertThat(account.getName(), is("Account Name"));
        Assert.assertThat(account.getBalance().getValue(), is(initialBalance.subtract(withdrawAmount)));
    }

    @Test(expected = InsufficientBalanceException.class)
    public void givenUnhealthyBalance_cannotWithdrawAmountFromAccount(){
        BigDecimal initialBalance = BigDecimal.valueOf(120.1);
        Account account = Account.createNewAccount("Account Name", Money.of(initialBalance, gbp));
        BigDecimal withdrawAmount = BigDecimal.valueOf(123.98);
        account.withdraw(Money.of(withdrawAmount, gbp));
    }


    @Test
    public void canDepositAmountIntoAccount(){
        BigDecimal initialBalance = BigDecimal.valueOf(1500.0);
        Account account = Account.createNewAccount("Account Name", Money.of(initialBalance, gbp));
        BigDecimal depositAmount = BigDecimal.valueOf(123.98);
        account.deposit(Money.of(depositAmount, gbp));
        Assert.assertThat(account, is(notNullValue()));
        Assert.assertThat(account.getName(), is("Account Name"));
        Assert.assertThat(account.getBalance().getValue(), is(initialBalance.add(depositAmount)));
    }

}
