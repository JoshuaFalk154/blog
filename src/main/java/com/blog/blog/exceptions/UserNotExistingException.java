package com.blog.blog.exceptions;

public class UserNotExistingException extends RuntimeException{
    public UserNotExistingException(String message) {
        super(message);
    }
}
