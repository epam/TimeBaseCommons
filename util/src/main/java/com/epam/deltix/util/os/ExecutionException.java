package com.epam.deltix.util.os;

import java.io.IOException;

public class ExecutionException extends IOException {
    private int errorCode;
    
    public ExecutionException() {
    }

    public ExecutionException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int          getErrorCode() {
        return errorCode;
    }
}
