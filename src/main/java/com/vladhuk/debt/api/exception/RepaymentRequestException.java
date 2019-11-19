package com.vladhuk.debt.api.exception;

public class RepaymentRequestException extends BadRequestException {
    public RepaymentRequestException(String message) {
        super(message);
    }

    public RepaymentRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
