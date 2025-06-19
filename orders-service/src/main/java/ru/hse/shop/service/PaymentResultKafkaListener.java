package ru.hse.shop.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.shop.dto.PaymentResultDTO;
import ru.hse.shop.entity.OrderEntity;
import ru.hse.shop.model.OrderStatus;
import ru.hse.shop.repository.OrderRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentResultKafkaListener {
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payments", groupId = "order-service-group")
    @Transactional
    public void handlePaymentResult(String message) {
        try {
            PaymentResultDTO result = objectMapper.readValue(message, PaymentResultDTO.class);
            OrderEntity order = orderRepository.findById(result.getId()).orElse(null);
            if (order == null) {
                return;
            }
            if ("OrderPaymentSuccess".equals(result.getEventType())) {
                order.setStatus(OrderStatus.COMPLETED);
            } else if ("OrderPaymentFailed".equals(result.getEventType())) {
                order.setStatus(OrderStatus.FAILED);
            }
            orderRepository.save(order);
        } catch (Exception _) { }
    }
}