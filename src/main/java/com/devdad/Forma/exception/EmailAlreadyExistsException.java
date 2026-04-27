package com.devdad.Forma.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EmailAlreadyExistsException(String msg) {
        super(msg);
    }
}
