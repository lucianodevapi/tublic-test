package com.marketinginapp.startup.ms.user.exception;

public class DuplicatedKeyException extends RuntimeException{
    public DuplicatedKeyException(String message) {
        super(message);
    }
}
