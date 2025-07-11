package com.blog.blog.dto;

import java.util.Date;

public record PostSingle(String title, String body, String authorEmail, Date createdAt, Date updatedAt) {
}
