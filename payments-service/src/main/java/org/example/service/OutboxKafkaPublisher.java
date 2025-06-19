package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.OutboxEventEntity;
import org.example.repository.OutboxEventRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxKafkaPublisher {
    private final OutboxEventRepository outboxRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${payments.outbox.kafka.topic:payments}")
    private String topic;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void publishOutboxEvents() {
        List<OutboxEventEntity> events = outboxRepo.findBySentFalse();
        for (OutboxEventEntity event : events) {
            try {
                kafkaTemplate.send(topic, event.getPayload());
                event.setSent(true);
                outboxRepo.save(event);
            } catch (Exception _) { }
        }
    }
} 