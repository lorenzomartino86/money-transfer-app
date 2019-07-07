package com.lmartino.bank.domain.model;

import com.lmartino.bank.domain.exception.InsufficientBalanceException;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TransferTest {

    @Test
    public void givenFooAccountAndBarAccount_whenMakeTransferFromFooToBar_thenAmountIsIncreasedInBarAndDecreasedInFoo(){
        // Given foo and bar accounts
        BigDecimal initialFooBalance = BigDecimal.valueOf(2500.00);
        Account foo = Account.createNewAccount("Foo", Amount.of(initialFooBalance), null);
        BigDecimal initialBarBalance = BigDecimal.valueOf(1345.98);
        Account bar = Account.createNewAccount("Foo", Amount.of(initialBarBalance), null);

        // When Make transfer from foo to bar of 35.99 euros
        BigDecimal transferAmount = BigDecimal.valueOf(35.99);
        Transfer transfer = Transfer.makeTransfer(foo, bar, Amount.of(transferAmount), "Test Transfer");

        // Then transfer amount is increased in Bar and decreased in Foo
        Assert.assertThat(transfer, is(notNullValue()));
        Assert.assertThat(foo.getBalance().getMoney(), is(initialFooBalance.subtract(transferAmount)));
        Assert.assertThat(bar.getBalance().getMoney(), is(initialBarBalance.add(transferAmount)));

    }

    @Test(expected = InsufficientBalanceException.class)
    public void givenFooAccountAndBarAccount_whenMakeTransferFromFooToBarAndFooHasNotSufficientBalance_thenTransferIsRejected(){
        // Given foo and bar accounts
        BigDecimal initialFooBalance = BigDecimal.valueOf(2500);
        Account foo = Account.createNewAccount("Foo", Amount.of(initialFooBalance), null);
        BigDecimal initialBarBalance = BigDecimal.valueOf(1345.98);
        Account bar = Account.createNewAccount("Foo", Amount.of(initialBarBalance), null);

        // When Make transfer from foo to bar of 35.99 euros
        BigDecimal transferAmount = BigDecimal.valueOf(3000);
        Transfer.makeTransfer(foo, bar, Amount.of(transferAmount), "Test Transfer");

    }

}