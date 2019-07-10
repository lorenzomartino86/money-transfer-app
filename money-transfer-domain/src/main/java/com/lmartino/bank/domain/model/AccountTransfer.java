package com.lmartino.bank.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.java.Log;

import java.time.LocalDateTime;

/**
 * Aggregation class to represent a Bank Account Transfer
 */
@Getter
@Log
@ToString
@EqualsAndHashCode
public class AccountTransfer {
    private Id id;
    private Id toAccountId;
    private Amount amount;
    private String description;
    private LocalDateTime createdAt;

    public AccountTransfer(Id id, Id toAccountId, Amount amount, String description, LocalDateTime createdAt) {
        this.id = id;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static AccountTransfer of(final Id id,
                                     final Id toAccountId,
                                     final Amount amount,
                                     final String description,
                                     final LocalDateTime createdAt){
        return new AccountTransfer(id, toAccountId, amount, description, createdAt);
    }
}
