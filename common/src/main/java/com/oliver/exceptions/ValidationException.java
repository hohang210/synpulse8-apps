package com.oliver.exceptions;

public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(
                message == null ?
                        "A validation error occurred" :
                        message
        );
    }
}
