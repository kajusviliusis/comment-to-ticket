package org.example.pulsedesk.controller;

import lombok.RequiredArgsConstructor;
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
    public List<Ticket> getAllTickets()
   {
       return ticketService.getAllTickets();
   }

   @GetMapping("/{id}")
    public Ticket getTicketById(@PathVariable Long id)
   {
       return ticketService.getTickedById(id);
   }

}
