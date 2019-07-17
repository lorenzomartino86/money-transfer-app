package com.lmartino.bank.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.java.Log;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.lmartino.bank.domain.exception.DomainExceptionHandler.insufficientBalanceException;

/**
 * Aggregation class to represent a Bank Account
 */
@Getter
@Log
@ToString
@EqualsAndHashCode
public class Account {
    private Id id;
    private String name;
    private Money balance;
    private LocalDateTime createdAt;

    private Account(final Id id,
                    final String name,
                    final Money balance,
                    final LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public static Account of(final Id id,
                             final String name,
                             final Money balance,
                             final LocalDateTime createdAt) {
        return new Account(id, name, balance, createdAt);
    }

    public static Account createNewAccount(final String name, final Money balance) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Account account = new Account(Id.create(), name, balance, now);
        log.info(String.format("Created new account %s", account));
        return account;
    }

    public synchronized void withdraw(Money money) {
        if (this.balance.isGreaterThan(money))
            this.balance.decreaseBy(money);
        else
            insufficientBalanceException(this.id.getValue());
    }

    public synchronized void deposit(Money money) {
        this.balance.increaseBy(money);
    }

    public boolean hasSameCurrency(Account other){
        return this.balance.getCurrency().getValue().equals(other.getBalance().getCurrency().getValue());
    }

    public Currency getCurrency(){
        return balance.getCurrency();
    }

    public boolean hasSameCurrency(Currency currency) {
        return balance.getCurrency().equals(currency);
    }
}
