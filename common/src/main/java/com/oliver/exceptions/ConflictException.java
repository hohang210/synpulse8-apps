package com.oliver.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String field, String message) {
        super(
                message == null ?
                        baseMessage(field) :
                        baseMessage(field) + " - " + message
        );
    }

    private static String baseMessage(String field) {
        return String.format("Conflict in field: %s", field);
    }
}
