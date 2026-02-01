package org.example.pulsedesk.mapper;

import org.example.pulsedesk.dto.TicketResponseDTO;
import org.example.pulsedesk.model.Ticket;

public class TicketMapper {
    public static TicketResponseDTO toDto(Ticket ticket) {
        return new TicketResponseDTO(
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
