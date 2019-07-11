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
    private String type;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String createdAt;

    public AccountTransferDto(final String id,
                              final String type,
                              final BigDecimal amount,
                              final String currency,
                              final String description,
                              final String createdAt){
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static AccountTransferDto of(final String id,
                                     final String type,
                                     final BigDecimal amount,
                                     final String currency,
                                     final String description,
                                     final LocalDateTime createdAt){
        return new AccountTransferDto(id,
                type, amount, currency, description, createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    }

}
