package org.example.pulsedesk.dto;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        String text,
        LocalDateTime createdAt
){}
