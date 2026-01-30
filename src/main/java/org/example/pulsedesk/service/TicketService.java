package org.example.pulsedesk.service;

import lombok.RequiredArgsConstructor;
import org.example.pulsedesk.model.Ticket;
import org.example.pulsedesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    public List<Ticket> getAllTickets()
    {
        return ticketRepository.findAll();
    }

    public Ticket getTickedById(Long id)
    {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }
}
