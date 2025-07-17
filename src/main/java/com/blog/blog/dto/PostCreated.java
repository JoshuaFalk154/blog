package com.blog.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public record PostCreated(@NotBlank String title, @NonNull UUID id, @NonNull LocalDate createdAt, String authorEmail) {
}
