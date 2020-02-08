package com.vladhuk.debt.api.exception;

public class NoOrdersException extends BadRequestException {

    public NoOrdersException(String message) {
        super(message);
    }

    public NoOrdersException(String message, Throwable cause) {
        super(message, cause);
    }

}
