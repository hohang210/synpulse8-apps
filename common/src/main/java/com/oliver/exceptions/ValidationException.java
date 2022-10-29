package com.oliver.exceptions;

public class ValidationException extends RuntimeException {
    private static final long serialVersionUID = 3587448184505530409L;

    public ValidationException(String message) {
        super(
                message == null ?
                        "A validation error occurred" :
                        message
        );
    }
}
