package com.blog.blog.dto;

import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record LikeCreated(@NonNull UUID id, @NonNull LocalDate createdAt, @NonNull UUID userId, @NonNull UUID postID) {
}
