package com.lmartino.bank.domain.model;

import com.lmartino.bank.domain.exception.InsufficientBalanceException;
import com.lmartino.bank.domain.model.Account;
import com.lmartino.bank.domain.model.Amount;
import com.lmartino.bank.domain.model.Transfer;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class TransferTest {

    @Test
    public void givenFooAccountAndBarAccount_whenMakeTransferFromFooToBar_thenAmountIsIncreasedInBarAndDecreasedInFoo(){
        // Given foo and bar accounts
        int initialFooBalance = 2500;
        Account foo = Account.createNewAccount("Foo", Amount.of(initialFooBalance));
        double initialBarBalance = 1345.98;
        Account bar = Account.createNewAccount("Foo", Amount.of(initialBarBalance));

        // When Make transfer from foo to bar of 35.99 euros
        double transferAmount = 35.99;
        Transfer transfer = Transfer.makeTransfer(foo, bar, Amount.of(transferAmount), "Test Transfer");

        // Then transfer amount is increased in Bar and decreased in Foo
        Assert.assertThat(transfer, is(notNullValue()));
        Assert.assertThat(foo.getBalance().getMoney(), is(initialFooBalance - transferAmount));
        Assert.assertThat(bar.getBalance().getMoney(), is(initialBarBalance + transferAmount));

    }

    @Test(expected = InsufficientBalanceException.class)
    public void givenFooAccountAndBarAccount_whenMakeTransferFromFooToBarAndFooHasNotSufficientBalance_thenTransferIsRejected(){
        // Given foo and bar accounts
        int initialFooBalance = 2500;
        Account foo = Account.createNewAccount("Foo", Amount.of(initialFooBalance));
        double initialBarBalance = 1345.98;
        Account bar = Account.createNewAccount("Foo", Amount.of(initialBarBalance));

        // When Make transfer from foo to bar of 35.99 euros
        double transferAmount = 3000;
        Transfer.makeTransfer(foo, bar, Amount.of(transferAmount), "Test Transfer");

    }

}