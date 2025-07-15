package com.blog.blog.exceptions;

public class LikeNotFoundException extends RuntimeException{
    public LikeNotFoundException(String message) {
        super(message);
    }
}
