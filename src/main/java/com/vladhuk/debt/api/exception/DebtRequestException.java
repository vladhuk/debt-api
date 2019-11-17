package com.vladhuk.debt.api.exception;

public class DebtRequestException extends BadRequestException {
    public DebtRequestException(String message) {
        super(message);
    }
    public DebtRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
