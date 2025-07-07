package com.blog.blog.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoad(@NotBlank String username, @NotBlank String sub) {
}
