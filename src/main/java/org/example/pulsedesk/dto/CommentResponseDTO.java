package org.example.pulsedesk.dto;

import java.time.LocalDateTime;

public record CommentResponseDTO(
        Long id,
        String text,
        LocalDateTime createdAt
){}
