package com.lmartino.bank.domain.exception;

import com.lmartino.bank.domain.model.Currency;
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

    public static void unknownCurrencyCodeException(final String currencyCode){
        throw new UnknownCurrencyCodeException(String.format("Unknwown currency code %s", currencyCode));
    }

    public static void illegalTransferCurrencyException(final Currency transferCurrency, final Currency validCurrency){
        throw new UnknownCurrencyCodeException(String.format("Cannot process the transfer with currency %s. " +
                "Only currency %s is allowed from the source account.", transferCurrency.getValue(), validCurrency.getValue()));
    }

    public static void accountNameConflictException(final String accountName){
        throw new AccountNameConflictException(String.format("Account with name %s already exist", accountName));
    }

}
