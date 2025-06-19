package ru.hse.shop.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.shop.entity.OrderOutboxEntity;
import ru.hse.shop.repository.OrderOutboxRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderOutboxKafkaPublisher {
    private static final Logger log = LoggerFactory.getLogger(OrderOutboxKafkaPublisher.class);
    private final OrderOutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${order.outbox.kafka.topic:orders}")
    private String topic;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void publishOutboxEvents() {
        List<OrderOutboxEntity> events = outboxRepository.findBySentFalse();
        for (OrderOutboxEntity event : events) {
            try {
                kafkaTemplate.send(topic, event.getPayload());
                event.setSent(true);
                outboxRepository.save(event);
            } catch (Exception e) { }
        }
    }
}