package com.quickpick.ureca.V2.userticket.repository;

import com.quickpick.ureca.V2.ticket.domain.TicketV2;
import com.quickpick.ureca.V2.user.domain.UserV2;
import com.quickpick.ureca.V2.userticket.domain.UserTicketV2;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTicketRepositoryV2 extends JpaRepository<UserTicketV2, Long> {
    boolean existsByUserAndTicket(UserV2 user, TicketV2 ticket);
    Optional<UserTicketV2> findByUserAndTicket(UserV2 user, TicketV2 ticket);
}