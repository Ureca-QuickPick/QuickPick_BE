package com.quickpick.ureca.v3.ticket.service;

import com.quickpick.ureca.v3.ticket.event.TicketPurchaseEventV3;
import com.quickpick.ureca.v3.ticket.kafka.TicketEventProducerV3;
import com.quickpick.ureca.v3.ticket.repository.RedisStockRepositoryV3;
import com.quickpick.ureca.v3.user.repository.UserRepositoryV3;
import com.quickpick.ureca.v3.userticket.repository.UserTicketRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceV3 {

    private final UserRepositoryV3 userRepositoryV3;
    private final RedisStockRepositoryV3 redisStockRepositoryV3;
    private final TicketEventProducerV3 ticketEventProducerV3;
    private final UserTicketRepositoryV3 userTicketRepositoryV3;

    public void purchaseTicket(final Long ticketId, final Long userId, final Long quantity) {

        if(!userRepositoryV3.existsById(userId)) {
            log.error("존재하지 않는 사용자입니다.");
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        if(userTicketRepositoryV3.existsByUserId(userId)) {
            log.error("이미 예약을 성공한 사용자입니다.");
            throw new RuntimeException("이미 예약을 성공한 사용자입니다.");
        }

        final Long result = redisStockRepositoryV3.decrease(ticketId, quantity);
        if (result == -1L) {
            throw new RuntimeException("티켓이 전부 소진되었습니다.");
        }

        final TicketPurchaseEventV3 ticketPurchaseEventV3 = TicketPurchaseEventV3.builder()
                .uuid(UUID.randomUUID().toString())
                .ticketId(ticketId)
                .userId(userId)
                .quantity(quantity).build();

        ticketEventProducerV3.send(ticketPurchaseEventV3);
    }
}
