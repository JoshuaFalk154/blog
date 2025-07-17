package com.blog.blog.dto;

import java.time.LocalDate;
import java.util.Date;

public record PostSingle(String title, String body, String authorEmail, LocalDate createdAt, Date updatedAt) {
}
