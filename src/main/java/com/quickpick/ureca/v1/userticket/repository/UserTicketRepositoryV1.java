package com.quickpick.ureca.v1.userticket.repository;

import com.quickpick.ureca.v1.userticket.domain.UserTicketV1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTicketRepositoryV1 extends JpaRepository<UserTicketV1, Long> {

    boolean existsByUser_UserIdAndTicket_TicketId(Long userId, Long ticketId);

    @Query(value = "SELECT 1 FROM user_ticket WHERE user_id = :userId AND ticket_id = :ticketId LIMIT 1", nativeQuery = true)
    Integer existsUserTicketRaw(@Param("userId") Long userId, @Param("ticketId") Long ticketId);



}
