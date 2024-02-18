package com.marketinginapp.startup.ms.user.exception;

public class AccessForbiddenException extends RuntimeException{
    public AccessForbiddenException(String message) {
        super(message);
    }
}
