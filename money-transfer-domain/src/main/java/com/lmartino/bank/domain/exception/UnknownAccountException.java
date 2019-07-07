package com.lmartino.bank.domain.exception;

public class UnknownAccountException extends RuntimeException {
    public UnknownAccountException(String message) {
        super(message);
    }
}
