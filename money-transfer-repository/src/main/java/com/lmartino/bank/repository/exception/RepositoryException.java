package com.lmartino.bank.repository.exception;

public class RepositoryException extends RuntimeException {

    public RepositoryException(Exception e){
        super(e);
    }
}
