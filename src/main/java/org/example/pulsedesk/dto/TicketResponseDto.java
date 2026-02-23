package org.example.pulsedesk.dto;

import java.time.LocalDateTime;

public record TicketResponseDto(
        Long id,
        String title,
        String category,
        String priority,
        String shortSummary,
        LocalDateTime createdAt,
        Long commentId
) {}
