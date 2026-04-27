package com.amigoscode.interview.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewRequest(
        @NotBlank(message = "Reviewer name is required")
        String reviewerName,

        @NotBlank(message = "Content cannot be empty")
        String content,

        @Min(1) @Max(5)
        Integer rating
) {}
