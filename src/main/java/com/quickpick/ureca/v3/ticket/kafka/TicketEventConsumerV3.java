package com.quickpick.ureca.v3.ticket.kafka;

import com.quickpick.ureca.v3.reserve.domain.ReserveV3;
import com.quickpick.ureca.v3.reserve.domain.ReserveStatusV3;
import com.quickpick.ureca.v3.ticket.event.TicketPurchaseEventV3;
import com.quickpick.ureca.v3.ticket.repository.ReserveRepositoryV3;
import com.quickpick.ureca.v3.ticket.repository.TicketRepositoryV3;
import com.quickpick.ureca.v3.userticket.domain.UserTicketV3;
import com.quickpick.ureca.v3.userticket.repository.UserTicketRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketEventConsumerV3 {

    private final TicketRepositoryV3 ticketRepositoryV3;
    private final UserTicketRepositoryV3 userTicketRepositoryV3;
    private final ReserveRepositoryV3 reserveRepositoryV3;

    @Transactional
    @KafkaListener(topics = "ticket.purchase", groupId = "ticket-service", concurrency = "3")
    public void consume(final TicketPurchaseEventV3 event, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {

        log.info("ticker.purchased 이벤트 수신, 수신한 아이디 : {}", event.getUserId());
        log.info("Consumed message from partition [{}] by thread [{}], userId: {}",
                partition,
                Thread.currentThread().getName(),
                event.getUserId());

        int result = ticketRepositoryV3.decreaseStock(event.getTicketId(), 1);
        if(result == 0) {
            throw new RuntimeException("티켓 수량 부족 및 존재하지 않음");
        }

        UserTicketV3 userTicketV3 = UserTicketV3.builder()
                .ticketId(event.getTicketId())
                .userId(event.getUserId())
                .build();
        userTicketRepositoryV3.save(userTicketV3);


        ReserveV3 reserveV3 = ReserveV3.builder()
                .userId(event.getUserId())
                .status(ReserveStatusV3.SUCCESS)
                .build();
        reserveRepositoryV3.save(reserveV3);
    }
}
