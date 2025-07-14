package com.blog.blog.exceptions;

public class UserNotOwnerException extends RuntimeException{
    public UserNotOwnerException(String message) {
        super(message);
    }
}
