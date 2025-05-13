package com.quickpick.ureca.v1.userticket.repository;

import com.quickpick.ureca.v1.userticket.domain.UserTicketV1;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserTicketShardingRepositoryV1 {

    private final EntityManager em;

    // Shard 선택
    private String getTableName(Long userId) {
        int shard = (int)(userId % 10);
        return "user_ticket_" + shard;
    }

    public boolean exists(Long userId, Long ticketId) {
        String tableName = getTableName(userId);
        String sql = "SELECT 1 FROM " + tableName +
                " WHERE user_id = :userId AND ticket_id = :ticketId LIMIT 1";

        List<?> result = em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("ticketId", ticketId)
                .getResultList();

        return !result.isEmpty();
    }

    public void saveIgnoreDuplicate(UserTicketV1 userTicketV1) {
        String tableName = getTableName(userTicketV1.getUser().getUserId());

        String sql = "INSERT IGNORE INTO " + tableName + " (user_id, ticket_id) " +
                "VALUES (:userId, :ticketId)";

        em.createNativeQuery(sql)
                .setParameter("userId", userTicketV1.getUser().getUserId())
                .setParameter("ticketId", userTicketV1.getTicket().getTicketId())
                .executeUpdate();
    }

    public void save(UserTicketV1 userTicketV1) {
        String tableName = getTableName(userTicketV1.getUser().getUserId());
        String sql = "INSERT INTO " + tableName + " (user_id, ticket_id) VALUES (:userId, :ticketId)";

        em.createNativeQuery(sql)
                .setParameter("userId", userTicketV1.getUser().getUserId())
                .setParameter("ticketId", userTicketV1.getTicket().getTicketId())
                .executeUpdate();
    }

    public void delete(Long userId, Long ticketId) {
        String tableName = getTableName(userId);
        String sql = "DELETE FROM " + tableName + " WHERE user_id = :userId AND ticket_id = :ticketId";

        em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("ticketId", ticketId)
                .executeUpdate();
    }

}
