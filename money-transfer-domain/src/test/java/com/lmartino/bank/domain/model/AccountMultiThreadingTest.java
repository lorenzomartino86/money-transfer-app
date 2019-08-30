package com.lmartino.bank.domain.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AccountMultiThreadingTest {


    @Test
    public void canDepositAmountInAccountThreadSafely() throws InterruptedException {
        Account account = Account.createNewAccount("MultiThread", Money.of(BigDecimal.ZERO, Currency.of("EUR")));
        int numThreads = 10000;
        Thread[] accountThreads = new Thread[numThreads];
        for (int i=1; i<=numThreads; i++){
            String threadName = "Thread " + i;
            DepositwAccountThread depositwAccountThread = new DepositwAccountThread(account, threadName);
            accountThreads[i-1] = new Thread(depositwAccountThread);
            accountThreads[i-1].start();
        }
        Thread.sleep(2000);
        assertThat(account.getBalance().getValue().intValue(), is(numThreads));
    }


    @Test
    public void canWithdrawAmountFromAccountThreadSafely() throws InterruptedException {
        int startingValue = 100000;
        Account account = Account.createNewAccount("MultiThread", Money.of(BigDecimal.valueOf(startingValue), Currency.of("EUR")));
        int numThreads = 10000;
        Thread[] accountThreads = new Thread[numThreads];
        for (int i=1; i<=numThreads; i++){
            String threadName = "Thread " + i;
            WithdrawAccountThread withdrawAccountThread = new WithdrawAccountThread(account, threadName);
            accountThreads[i-1] = new Thread(withdrawAccountThread);
            accountThreads[i-1].start();
        }
        Thread.sleep(2000);
        assertThat(account.getBalance().getValue().intValue(), is(startingValue - numThreads));
    }


    public static class WithdrawAccountThread implements Runnable {
        private Account account;
        private String name;

        public WithdrawAccountThread(Account account, String name) {
            this.account = account;
            this.name = name;
        }

        @Override
        public void run() {
            // System.out.println("Thread: " + Thread.currentThread().getId());
            account.withdraw(Money.of(BigDecimal.ONE, Currency.of("EUR")));
        }

    }

    public static class DepositwAccountThread implements Runnable {
        private Account account;
        private String name;

        public DepositwAccountThread(Account account, String name) {
            this.account = account;
            this.name = name;
        }

        @Override
        public void run() {
            // System.out.println("Thread: " + Thread.currentThread().getId());
            account.deposit(Money.of(BigDecimal.ONE, Currency.of("EUR")));
        }

    }

}