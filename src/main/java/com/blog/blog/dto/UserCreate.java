package com.blog.blog.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

public record UserCreate(@NotBlank String email, @NotBlank String sub) {
}
