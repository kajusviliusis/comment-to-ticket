package org.example.pulsedesk.repository;

import org.example.pulsedesk.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long>
{

}
