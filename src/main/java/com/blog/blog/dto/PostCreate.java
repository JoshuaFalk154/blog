package com.blog.blog.dto;

import jakarta.validation.constraints.NotBlank;

public record PostCreate(@NotBlank String title, @NotBlank String body) {
}
