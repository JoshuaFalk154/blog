package com.blog.blog.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record PostFull(String title, String body, UUID id, String authorEmail, LocalDate createdAt, LocalDate updatedAt, long numberOfLikes) {
}
