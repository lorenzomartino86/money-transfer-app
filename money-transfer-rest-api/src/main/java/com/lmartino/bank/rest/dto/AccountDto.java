package com.lmartino.bank.rest.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@Setter
public class AccountDto {
    private String id;
    private String name;
    private BigDecimal balance;
    private String createdAt;

    private AccountDto(final String id, final String name, final BigDecimal balance, final String createdAt){
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public static AccountDto of(final String id, final String name, final BigDecimal balance, final LocalDateTime createdAt){
        return new AccountDto(id, name, balance, createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

}
