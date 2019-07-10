package com.lmartino.bank.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class AccountTransferDto {
    private String id;
    private String toAccountId;
    private BigDecimal amount;
    private String description;
    private String createdAt;

    public AccountTransferDto(final String id,
                              final String toAccountId,
                              final BigDecimal amount,
                              final String description,
                              final String createdAt){
        this.id = id;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static AccountTransferDto of(final String id,
                                     final String toAccountId,
                                     final BigDecimal amount,
                                     final String description,
                                     final LocalDateTime createdAt){
        return new AccountTransferDto(id, toAccountId, amount, description, createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    }

}
