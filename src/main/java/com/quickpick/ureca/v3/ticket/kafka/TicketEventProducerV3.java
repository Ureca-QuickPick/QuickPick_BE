package com.quickpick.ureca.v3.ticket.kafka;

import com.quickpick.ureca.v3.ticket.event.TicketPurchaseEventV3;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketEventProducerV3 {

    private final KafkaTemplate<String, TicketPurchaseEventV3> kafkaTemplate;
    private final String TOPIC = "ticket.purchase";

    public void send(final TicketPurchaseEventV3 ticketPurchaseEventV3) {
        kafkaTemplate.send(TOPIC, ticketPurchaseEventV3);
    }
}
