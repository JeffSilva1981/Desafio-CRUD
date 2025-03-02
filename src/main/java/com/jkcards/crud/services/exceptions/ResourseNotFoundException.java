package com.jkcards.crud.services.exceptions;

public class ResourseNotFoundException extends RuntimeException {

    public ResourseNotFoundException(String msn) {
        super(msn);
    }
}
