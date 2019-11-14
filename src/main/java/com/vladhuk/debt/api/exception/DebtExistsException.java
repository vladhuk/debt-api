package com.vladhuk.debt.api.exception;

public class DebtExistsException extends BadRequestException {
    public DebtExistsException(String message) {
        super(message);
    }
    public DebtExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
