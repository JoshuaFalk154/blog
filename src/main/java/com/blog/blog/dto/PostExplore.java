package com.blog.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record PostExplore(@NotBlank String title, @NonNull UUID id, @NonNull LocalDate createdAt, @Email String authorEmail, long numberOfLikes) {
}