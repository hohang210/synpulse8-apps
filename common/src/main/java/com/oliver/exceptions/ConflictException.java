package com.oliver.exceptions;

public class ConflictException extends RuntimeException {
    private static final long serialVersionUID = -5297767126678246357L;

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
