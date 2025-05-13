package com.quickpick.ureca.V2.ticket.repository;

import com.quickpick.ureca.V2.ticket.domain.TicketV2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepositoryV2 extends JpaRepository<TicketV2, Long> {
}
