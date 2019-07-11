package com.lmartino.bank.domain.exception;

public class IllegalTransferCurrencyException extends RuntimeException {

    public IllegalTransferCurrencyException(String message){
        super(message);
    }
}
