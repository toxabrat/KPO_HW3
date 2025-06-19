package ru.hse.shop.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.hse.shop.dto.request.AccountDTO;
import ru.hse.shop.dto.request.OrderCreateDTO;
import ru.hse.shop.dto.response.OrderDTO;
import ru.hse.shop.entity.OrderEntity;
import ru.hse.shop.model.OrderStatus;
import ru.hse.shop.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.shop.entity.OrderOutboxEntity;
import ru.hse.shop.repository.OrderOutboxRepository;
import java.time.Instant;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderOutboxRepository orderOutboxRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${file.storage.url}")
    private String paymentsService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public OrderDTO createOrder(OrderCreateDTO orderDTO) {

        OrderEntity ans = orderRepository.save(
                OrderEntity.builder()
                        .senderId(orderDTO.senderId())
                        .receiverId(orderDTO.receiverId())
                        .transactionAmount(orderDTO.transactionAmount()).build());

        try {
            String payload = objectMapper.writeValueAsString(ans);
            OrderOutboxEntity outbox = OrderOutboxEntity.builder()
                    .eventType("OrderCreated")
                    .payload(payload)
                    .createdAt(Instant.now())
                    .sent(false)
                    .build();
            orderOutboxRepository.save(outbox);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize order for outbox", e);
        }

        return buildOrderDTOFromOrderEntity(ans);
    }

    public List<OrderDTO> allOrders() {
        List<OrderEntity> ans = orderRepository.findAll();
        return ans.stream()
                .map((item) -> new OrderDTO(item.getId(),
                        item.getSenderId(),
                        item.getReceiverId(),
                        item.getTransactionAmount(),
                        item.getStatus()))
                .toList();
    }

    public OrderDTO getStatus(Long id) {
        OrderEntity orderEntity = orderRepository.getReferenceById(id);
        return buildOrderDTOFromOrderEntity(orderEntity);
    }

    public OrderDTO setStatus(Long id, OrderStatus status) {
        OrderEntity orderEntity = orderRepository.getReferenceById(id);
        orderEntity.setStatus(status);
        return buildOrderDTOFromOrderEntity(orderRepository.save(orderEntity));
    }

    private void addAccount(Long userId) {
        try {
            restTemplate.getForObject(
                    paymentsService + "/balance/" + userId,
                    AccountDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            restTemplate.put(paymentsService + "/balance/" + userId, null);
        }
    }

    private OrderDTO buildOrderDTOFromOrderEntity(OrderEntity orderEntity) {
        return new OrderDTO(
                orderEntity.getId(),
                orderEntity.getSenderId(),
                orderEntity.getReceiverId(),
                orderEntity.getTransactionAmount(),
                orderEntity.getStatus());
    }
}
