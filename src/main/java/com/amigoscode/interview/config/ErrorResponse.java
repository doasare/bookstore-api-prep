package com.amigoscode.interview.config;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String status;
    private Map<String, String> message;
    private String path;
}
