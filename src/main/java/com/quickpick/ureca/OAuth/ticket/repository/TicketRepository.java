package com.quickpick.ureca.OAuth.ticket.repository;

import com.quickpick.ureca.OAuth.ticket.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
