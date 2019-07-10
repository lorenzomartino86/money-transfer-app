package com.lmartino.bank.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.java.Log;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@ToString
@Log
@EqualsAndHashCode
public class Transfer {
    private Id id;
    private Account fromAccount;
    private Account toAccount;
    private Amount amount;
    private String description;
    private LocalDateTime createdAt;

    public Transfer(final Id id,
                    final Account fromAccount,
                    final Account toAccount,
                    final Amount amount,
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
                              final Amount amount,
                              final String description,
                              final LocalDateTime createdAt){
        return  new Transfer(id, fromAccount, toAccount, amount, description, createdAt);
    }

    public static Transfer makeTransfer(final Account fromAccount,
                                        final Account toAccount,
                                        final Amount amount,
                                        final String description,
                                        final BigDecimal rate){
        fromAccount.withdraw(amount);
        toAccount.deposit(amount.applyRate(rate));
        LocalDateTime createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Transfer transfer = new Transfer(Id.create(), fromAccount, toAccount, amount, description, createdAt);
        log.info(String.format("Created transfer %s", transfer));
        return transfer;
    }

}
