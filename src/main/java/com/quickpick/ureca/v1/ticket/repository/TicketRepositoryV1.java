package com.quickpick.ureca.v1.ticket.repository;

import com.quickpick.ureca.v1.ticket.domain.TicketV1;
import com.quickpick.ureca.v1.ticket.projection.TicketQuantityProjectionV1;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TicketRepositoryV1 extends JpaRepository<TicketV1, Long> {

    // 비관적 락
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select t from TicketV1 t where t.ticketId = :ticketId""")
    Optional<TicketV1> findByIdForUpdate(Long ticketId);

    // 비관적 락 (네이티브 쿼리)
    @Query(value = "SELECT * FROM ticket WHERE ticket_id = :ticketId FOR UPDATE", nativeQuery = true)
    TicketV1 findByIdForUpdateNative(@Param("ticketId") Long ticketId);


    // open-in-view + FetchJoin + DTO + 비관적 락
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select t from TicketV1 t
    left join fetch t.userTicketV1s ut
    left join fetch ut.user
    where t.ticketId = :ticketId
    """)
    Optional<TicketV1> findByIdForUpdateWithUsers(Long ticketId);


    // Projection 기반 조회
    @Query(value = "SELECT ticket_id AS ticketId, quantity AS quantity FROM ticket WHERE ticket_id = :ticketId FOR UPDATE", nativeQuery = true)
    TicketQuantityProjectionV1 findQuantityForUpdate(@Param("ticketId") Long ticketId);

    @Modifying
    @Query(value = "UPDATE ticket SET quantity = quantity - 1 WHERE ticket_id = :ticketId", nativeQuery = true)
    void decreaseQuantity(@Param("ticketId") Long ticketId);


    @Modifying
    @Transactional
    @Query(value = "UPDATE ticket SET quantity = quantity - 1 WHERE ticket_id = :ticketId AND quantity > 0", nativeQuery = true)
    int decreaseQuantityIfAvailable(@Param("ticketId") Long ticketId);
}
