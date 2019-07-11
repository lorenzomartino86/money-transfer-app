package com.lmartino.bank.domain.exception;

public class AccountNameConflictException extends RuntimeException {

    public AccountNameConflictException(String message){
        super(message);
    }
}
