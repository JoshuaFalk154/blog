package com.blog.blog.dto;

import jakarta.validation.constraints.NotBlank;

public record UserCreate(@NotBlank String username, @NotBlank String sub) {
}
