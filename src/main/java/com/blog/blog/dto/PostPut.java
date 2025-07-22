package com.blog.blog.dto;

import com.blog.blog.entities.Post;

public record PostPut(Post post, PutResponse putResponse) {
}
