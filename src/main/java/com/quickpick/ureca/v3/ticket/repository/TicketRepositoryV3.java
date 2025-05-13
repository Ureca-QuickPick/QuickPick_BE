package com.quickpick.ureca.v3.ticket.repository;

import com.quickpick.ureca.v3.ticket.domain.TicketV3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepositoryV3 extends JpaRepository<TicketV3, Long> {

    @Modifying
    @Query(value = "UPDATE tickets SET quantity = quantity - :amount WHERE ticket_id = :ticketId", nativeQuery = true)
    int decreaseStock(@Param("ticketId") Long ticketId, @Param("amount") int amount);
}