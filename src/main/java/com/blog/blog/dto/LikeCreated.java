package com.blog.blog.dto;

import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.UUID;

public record LikeCreated(@NonNull UUID id, @NonNull Date createdAt, @NonNull UUID userId, @NonNull UUID postID) {
}
