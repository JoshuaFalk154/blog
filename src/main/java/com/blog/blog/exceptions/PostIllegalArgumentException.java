package com.blog.blog.exceptions;

public class PostIllegalArgumentException extends RuntimeException {
    public PostIllegalArgumentException(String message) {
        super(message);
    }
}
