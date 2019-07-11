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
    private TransferType type;
    private Money money;
    private String description;
    private LocalDateTime createdAt;

    public AccountTransfer(Id id, TransferType type, Money money, String description, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.money = money;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static AccountTransfer of(final Id id,
                                     final TransferType type,
                                     final Money money,
                                     final String description,
                                     final LocalDateTime createdAt){
        return new AccountTransfer(id, type, money, description, createdAt);
    }
}
