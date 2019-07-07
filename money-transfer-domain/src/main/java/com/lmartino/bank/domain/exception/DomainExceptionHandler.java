package com.lmartino.bank.domain.exception;

import com.lmartino.bank.domain.model.Transfer;

import static java.lang.String.format;

public final class DomainExceptionHandler {

    public static void unkownAccountException(final String accountId){
        throw new UnknownAccountException(format("Account with id %s is unknown", accountId));
    }

    public static void insufficientBalanceException(final String accountId){
        throw new InsufficientBalanceException(format("Account with id %s has not enough balance to proceed with bank transfer", accountId));
    }

    public static void unprocessableTransferException(final Transfer transfer){
        throw new UnprocessableTransferException(String.format("Error during transfer transaction with details %s", transfer));
    }

}
