package com.blog.blog.dto;

import java.util.Date;
import java.util.UUID;

public record PostFull(String title, String body, UUID id, String authorEmail, Date createdAt, Date updatedAt, long numberOfLikes) {
}
