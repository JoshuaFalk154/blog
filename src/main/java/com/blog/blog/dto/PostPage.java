package com.blog.blog.dto;

import java.util.List;

public record PostPage<T>(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages) {
}
