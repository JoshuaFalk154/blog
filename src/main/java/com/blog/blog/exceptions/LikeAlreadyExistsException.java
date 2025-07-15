package com.blog.blog.exceptions;

public class LikeAlreadyExistsException extends RuntimeException{
    public LikeAlreadyExistsException(String message) {
        super(message);
    }
}
