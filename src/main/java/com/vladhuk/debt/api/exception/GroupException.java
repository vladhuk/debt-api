package com.vladhuk.debt.api.exception;

public class GroupException extends BadRequestException {
    public GroupException(String message) {
        super(message);
    }

    public GroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
