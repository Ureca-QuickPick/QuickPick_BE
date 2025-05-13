package com.quickpick.ureca.v3.config;

import com.quickpick.ureca.v3.ticket.event.TicketPurchaseEventV3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
public class KafkaConsumerConfigV3 {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TicketPurchaseEventV3> batchFactory(
            ConsumerFactory<String, TicketPurchaseEventV3> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, TicketPurchaseEventV3> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true); // 핵심
        factory.setConcurrency(3);
        return factory;
    }
}
