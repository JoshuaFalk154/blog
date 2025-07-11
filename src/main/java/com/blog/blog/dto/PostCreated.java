package com.blog.blog.dto;

import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.UUID;

public record PostCreated(@NonNull UUID id, @NonNull Date createdAt, UUID authorId) {
}
