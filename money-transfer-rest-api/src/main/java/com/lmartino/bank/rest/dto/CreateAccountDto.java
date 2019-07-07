package com.lmartino.bank.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDto {
    private String name;
    private BigDecimal balance;
    private String currency;
}
