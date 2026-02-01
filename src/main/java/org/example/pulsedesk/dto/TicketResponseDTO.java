package org.example.pulsedesk.dto;

import java.time.LocalDateTime;

public record TicketResponseDTO(
        Long id,
        String title,
        String category,
        String priority,
        String shortSummary,
        LocalDateTime createdAt
) {}
