package com.lmartino.bank.domain.model;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.java.Log;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.lmartino.bank.domain.exception.DomainExceptionHandler.illegalTransferCurrencyException;

@Getter
@ToString
@Log
public class Transfer {
    private Id id;
    private Account fromAccount;
    private Account toAccount;
    private Money withdrawAmount;
    private Money depositAmount;
    private BigDecimal exchangeRate;
    private String description;
    private LocalDateTime createdAt;

    public Transfer(final Id id,
                    final Account fromAccount,
                    final Account toAccount,
                    final Money withdrawAmount,
                    final Money depositAmount,
                    final BigDecimal exchangeRate,
                    final String description,
                    final LocalDateTime createdAt) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.withdrawAmount = withdrawAmount;
        this.depositAmount = depositAmount;
        this.exchangeRate = exchangeRate;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static Transfer of(final Id id,
                              final Account fromAccount,
                              final Account toAccount,
                              final Money withdrawAmount,
                              final Money depositAmount,
                              final BigDecimal exchangeRate,
                              final String description,
                              final LocalDateTime createdAt){
        return  new Transfer(id, fromAccount, toAccount, withdrawAmount, depositAmount, exchangeRate, description, createdAt);
    }

    public static Transfer makeTransfer(final Account fromAccount,
                                        final Account toAccount,
                                        final Money withdrawAmount,
                                        final String description,
                                        final BigDecimal rate){
        if (!fromAccount.hasSameCurrency(withdrawAmount.getCurrency()))
            illegalTransferCurrencyException(withdrawAmount.getCurrency(), fromAccount.getBalance().getCurrency());

        fromAccount.withdraw(withdrawAmount);

        Money depositAmount = withdrawAmount.applyRate(rate, toAccount.getCurrency());
        toAccount.deposit(depositAmount);

        LocalDateTime createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Transfer transfer = new Transfer(Id.create(), fromAccount, toAccount,
                withdrawAmount, depositAmount, rate, description, createdAt);
        log.info(String.format("Created transfer %s", transfer));
        return transfer;
    }

}
