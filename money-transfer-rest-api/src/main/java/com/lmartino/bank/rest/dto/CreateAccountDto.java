package com.lmartino.bank.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAccountDto {
    private String name;
    private Double balance;
}
