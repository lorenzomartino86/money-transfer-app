package com.lmartino.bank.domain.exception;

public class UnknownCurrencyCodeException extends RuntimeException {

    public UnknownCurrencyCodeException(String messsage){
        super(messsage);
    }
}
