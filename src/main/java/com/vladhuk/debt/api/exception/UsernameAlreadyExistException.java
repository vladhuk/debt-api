package com.vladhuk.debt.api.exception;

public class UsernameAlreadyExistException extends BadRequestException {
    public UsernameAlreadyExistException(String message) {
        super(message);
    }
    public UsernameAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
