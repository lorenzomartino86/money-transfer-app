package com.lmartino.bank.domain.exception;

public class UnknownCurrencyCodeException extends RuntimeException {

    public UnknownCurrencyCodeException(String message){
        super(message);
    }
}
