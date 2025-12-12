package com.epam.deltix.util.lang;

public class NotFoundException extends Exception {

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
    }   
    
}
