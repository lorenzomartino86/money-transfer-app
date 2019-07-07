package com.lmartino.bank.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class TransferDto {
    private String id;
    private String fromAccountId;
    private String toAccountId;
    private String description;
    private BigDecimal amount;
    private String createdAt;

    private TransferDto(final String id,
                        final String fromAccountId,
                        final String toAccountId,
                        final String description,
                        final BigDecimal amount,
                        final String createdAt){
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.description = description;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static TransferDto of(final String id,
                                 final String fromAccountId,
                                 final String toAccountId,
                                 final String description,
                                 final BigDecimal amount,
                                 final LocalDateTime createdAt){
        return new TransferDto(id, fromAccountId, toAccountId, description, amount, createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

}
