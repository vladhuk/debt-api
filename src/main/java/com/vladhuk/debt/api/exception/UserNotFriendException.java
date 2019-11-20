package com.vladhuk.debt.api.exception;

public class UserNotFriendException extends BadRequestException {
    public UserNotFriendException(String message) {
        super(message);
    }
    public UserNotFriendException(String message, Throwable cause) {
        super(message, cause);
    }
}
