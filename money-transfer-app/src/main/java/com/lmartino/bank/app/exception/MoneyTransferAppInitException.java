package com.lmartino.bank.app.exception;

public class MoneyTransferAppInitException extends RuntimeException {
    public MoneyTransferAppInitException(Exception e) {
        super(e);
    }
}
