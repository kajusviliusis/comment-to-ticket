package org.example.pulsedesk.controller;

import lombok.RequiredArgsConstructor;
import org.example.pulsedesk.dto.TicketResponseDto;
import org.example.pulsedesk.mapper.TicketMapper;
import org.example.pulsedesk.model.Ticket;
import org.example.pulsedesk.service.TicketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
   private final TicketService ticketService;

   @GetMapping
    public List<TicketResponseDto> getAllTickets()
   {
       List<Ticket> tickets = ticketService.getAllTickets();
       return tickets.stream().map(TicketMapper::toDto).toList();
   }

   @GetMapping("/{id}")
    public TicketResponseDto getTicketById(@PathVariable Long id)
   {
       Ticket ticket = ticketService.getTicketById(id);
       return TicketMapper.toDto(ticket);
   }

}
