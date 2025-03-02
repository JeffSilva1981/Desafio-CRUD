package com.jkcards.crud.services.exceptions;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String msn) {
        super(msn);
    }
}
