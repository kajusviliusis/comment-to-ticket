package org.example.pulsedesk.mapper;

import org.example.pulsedesk.dto.TicketResponseDto;
import org.example.pulsedesk.model.Ticket;

public class TicketMapper {
    public static TicketResponseDto toDto(Ticket ticket) {
        return new TicketResponseDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getCategory(),
                ticket.getPriority(),
                ticket.getShortSummary(),
                ticket.getCreatedAt(),
                ticket.getComment() != null ? ticket.getComment().getId() : null
        );
    }
}
