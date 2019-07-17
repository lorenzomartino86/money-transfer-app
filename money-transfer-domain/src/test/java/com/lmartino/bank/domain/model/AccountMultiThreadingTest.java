package com.lmartino.bank.domain.model;

import com.lmartino.bank.domain.MultithreadTestSupport;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AccountMultiThreadingTest {


    @Test
    public void canDepositAmountInAccountThreadSafely() throws InterruptedException {
        Account account = Account.createNewAccount("MultiThread", Money.of(BigDecimal.ZERO, Currency.of("EUR")));
        MultithreadTestSupport stressTester = new MultithreadTestSupport(10000);

        stressTester.init(new Runnable() {
            public void run() {
                account.deposit(Money.of(BigDecimal.ONE, Currency.of("EUR")));
            }
        });

        stressTester.shutdown();

        assertThat(account.getBalance().getValue().intValue(), is(stressTester.totalActionCount()));
    }


    @Test
    public void canWithdrawAmountFromAccountThreadSafely() throws InterruptedException {
        int startingValue = 100000;
        Account account = Account.createNewAccount("MultiThread", Money.of(BigDecimal.valueOf(startingValue), Currency.of("EUR")));
        MultithreadTestSupport multithreadEnv = new MultithreadTestSupport(10000);

        multithreadEnv.init(new Runnable() {
            public void run() {
                account.withdraw(Money.of(BigDecimal.ONE, Currency.of("EUR")));
            }
        });

        multithreadEnv.shutdown();

        assertThat(account.getBalance().getValue().intValue(), is(startingValue - multithreadEnv.totalActionCount()));
    }

}