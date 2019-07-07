package com.lmartino.bank.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreateAccountDto {
    private String name;
    private BigDecimal balance;
}
