package com.oliver.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(
                message == null ?
                        "A validation error occurred" :
                        message
        );
    }
}
