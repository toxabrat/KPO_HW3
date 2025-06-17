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

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Value("${file.storage.url}")
    private String paymentsService;

    private final RestTemplate restTemplate = new RestTemplate();

    public OrderDTO createOrder(OrderCreateDTO orderDTO) {
        addAccount(orderDTO.senderId());
        addAccount(orderDTO.receiverId());

        restTemplate.postForObject(
                paymentsService + "/balance/write/off/money/" + orderDTO.senderId(),
                orderDTO.transactionAmount(),
                AccountDTO.class
        );
        restTemplate.postForObject(
                paymentsService + "/balance/put/money/" + orderDTO.receiverId(),
                orderDTO.transactionAmount(),
                AccountDTO.class
        );

        OrderEntity ans = orderRepository.save(
                OrderEntity.builder()
                        .senderId(orderDTO.senderId())
                        .receiverId(orderDTO.receiverId())
                        .transactionAmount(orderDTO.transactionAmount()).build()
        );

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
                    AccountDTO.class
            );
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
