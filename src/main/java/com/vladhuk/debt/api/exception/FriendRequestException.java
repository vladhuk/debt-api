package com.vladhuk.debt.api.exception;

public class FriendRequestException extends BadRequestException {
    public FriendRequestException(String message) {
        super(message);
    }
    public FriendRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
