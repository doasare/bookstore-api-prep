package com.amigoscode.interview.review;

import lombok.Data;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String reviewerName,
        String content,
        Integer rating,
        LocalDateTime createdAt
) {}
