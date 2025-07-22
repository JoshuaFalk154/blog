package com.blog.blog.dto;

import jakarta.validation.constraints.NotEmpty;

public record PostUpdate(@NotEmpty String title, @NotEmpty String body) {
}
