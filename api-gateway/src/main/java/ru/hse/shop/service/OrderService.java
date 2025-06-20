package ru.hse.shop.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.hse.shop.dto.OrderCreateDTO;
import ru.hse.shop.dto.OrderDTO;
import ru.hse.shop.model.OrderStatus;

import java.util.List;

@Service
public class OrderService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${services.orders-service.url}")
    private String ordersService;

    public List<OrderDTO> allOrders() {
        ResponseEntity<List<OrderDTO>> response = restTemplate.exchange(
                ordersService + "/orders/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<OrderDTO>>() {}
        );

        return response.getBody();
    }

    public OrderDTO createOrder(OrderCreateDTO orderDTO) {
        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                ordersService + "/orders/add",
                HttpMethod.PUT,
                new HttpEntity<>(orderDTO),
                OrderDTO.class
        );
        return response.getBody();
    }
}
