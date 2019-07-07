package com.lmartino.bank.domain.exception;

public class UnprocessableTransferException extends RuntimeException {
    public UnprocessableTransferException(String message) {
        super(message);
    }
}
