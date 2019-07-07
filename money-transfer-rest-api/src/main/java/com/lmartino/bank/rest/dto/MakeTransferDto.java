package com.lmartino.bank.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MakeTransferDto {
    private String fromAccountId;
    private String toAccountId;
    private Double amount;
    private String description;

}
