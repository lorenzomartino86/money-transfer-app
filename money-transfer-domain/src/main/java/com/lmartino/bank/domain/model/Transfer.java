package com.lmartino.bank.domain.model;

import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class Transfer {
    private Id id;
    private Account fromAccount;
    private Account toAccount;
    private Money amount;
    private String description;
    private LocalDateTime createdAt;

    public Transfer(final Id id,
                    final Account fromAccount,
                    final Account toAccount,
                    final Money amount,
                    final String description,
                    final LocalDateTime createdAt) {
        this.id = id;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static Transfer of(final Id id,
                              final Account fromAccount,
                              final Account toAccount,
                              final Money money,
                              final String description,
                              final LocalDateTime createdAt){
        return  new Transfer(id, fromAccount, toAccount, money, description, createdAt);
    }

    public static Transfer makeTransfer(final Account fromAccount,
                                        final Account toAccount,
                                        final Money transferAmount,
                                        final String description,
                                        final BigDecimal rate){
        if (!fromAccount.hasSameCurrency(transferAmount.getCurrency()))
            illegalTransferCurrencyException(transferAmount.getCurrency(), fromAccount.getBalance().getCurrency());

        fromAccount.withdraw(transferAmount);
        toAccount.deposit(transferAmount.applyRate(rate));

        LocalDateTime createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Transfer transfer = new Transfer(Id.create(), fromAccount, toAccount, transferAmount, description, createdAt);
        log.info(String.format("Created transfer %s", transfer));
        return transfer;
    }

}
