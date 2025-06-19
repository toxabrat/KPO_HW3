package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.entity.InboxEventEntity;
import org.example.entity.OutboxEventEntity;
import org.example.repository.InboxEventRepository;
import org.example.repository.OutboxEventRepository;
import org.example.dto.OrderEventDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventKafkaListener {
    private final InboxEventRepository inboxRepo;
    private final OutboxEventRepository outboxRepo;
    private final ObjectMapper objectMapper;
    private final AccountService accountService;

    @KafkaListener(topics = "orders", groupId = "payments-service-group")
    @Transactional
    public void handleOrderEvent(ConsumerRecord<String, String> record) {
        String eventId = record.topic() + ":" + record.partition() + ":" + record.offset();

        if (inboxRepo.existsById(eventId)) {
            return;
        }

        InboxEventEntity inbox = InboxEventEntity.builder()
                .eventId(eventId)
                .eventType("OrderCreated")
                .payload(record.value())
                .receivedAt(Instant.now())
                .build();
        inboxRepo.save(inbox);

        OrderEventDTO order;
        try {
            order = objectMapper.readValue(record.value(), OrderEventDTO.class);
        } catch (Exception e) {
            return;
        }
        boolean success = false;
        String failReason = null;
        try {
            accountService.writeOffMoney(order.getSenderId(), order.getTransactionAmount());
            accountService.putMoney(order.getReceiverId(), order.getTransactionAmount());
            success = true;
        } catch (Exception e) { }
        String resultType = success ? "OrderPaymentSuccess" : "OrderPaymentFailed";

        String resultPayload;
        try {
            org.example.dto.PaymentResultDTO resultDto = new org.example.dto.PaymentResultDTO(
                order.getId(),
                order.getSenderId(),
                order.getReceiverId(),
                order.getTransactionAmount(),
                order.getStatus(),
                resultType
            );
            resultPayload = objectMapper.writeValueAsString(resultDto);
        } catch (Exception e) {
            resultPayload = record.value();
        }

        OutboxEventEntity outbox = OutboxEventEntity.builder()
                .eventType(resultType)
                .payload(resultPayload)
                .createdAt(Instant.now())
                .sent(false)
                .build();
        outboxRepo.save(outbox);
    }
} 