package com.lmartino.bank.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MakeTransferDto {
    private String fromAccountId;
    private String toAccountId;
    private BigDecimal amount;
    private String description;

}
