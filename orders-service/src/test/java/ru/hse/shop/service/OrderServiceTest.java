package ru.hse.shop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.hse.shop.dto.request.OrderCreateDTO;
import ru.hse.shop.dto.response.OrderDTO;
import ru.hse.shop.entity.OrderEntity;
import ru.hse.shop.model.OrderStatus;
import ru.hse.shop.repository.OrderOutboxRepository;
import ru.hse.shop.repository.OrderRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderOutboxRepository orderOutboxRepository;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        orderService.orderRepository = orderRepository;
        orderService.orderOutboxRepository = orderOutboxRepository;
        Field omField = OrderService.class.getDeclaredField("objectMapper");
        omField.setAccessible(true);
        omField.set(orderService, objectMapper);
    }

    @Test
    void testCreateOrder() throws JsonProcessingException {
        OrderCreateDTO dto = new OrderCreateDTO(1L, 2L, 100L);
        OrderEntity entity = OrderEntity.builder()
                .id(1L)
                .senderId(1L)
                .receiverId(2L)
                .transactionAmount(100L)
                .status(OrderStatus.CREATED)
                .build();
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(entity);
        when(objectMapper.writeValueAsString(any())).thenReturn("{}{}");

        OrderDTO result = orderService.createOrder(dto);
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(OrderStatus.CREATED, result.orderStatus());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(orderOutboxRepository, times(1)).save(any());
    }

    @Test
    void testSetStatus() {
        OrderEntity entity = OrderEntity.builder()
                .id(1L)
                .senderId(1L)
                .receiverId(2L)
                .transactionAmount(100L)
                .status(OrderStatus.CREATED)
                .build();
        when(orderRepository.getReferenceById(1L)).thenReturn(entity);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(entity);

        OrderDTO result = orderService.setStatus(1L, OrderStatus.FAILED);
        assertNotNull(result);
        assertEquals(OrderStatus.FAILED, result.orderStatus());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void testGetStatus() {
        OrderEntity entity = OrderEntity.builder()
                .id(1L)
                .senderId(1L)
                .receiverId(2L)
                .transactionAmount(100L)
                .status(OrderStatus.COMPLETED)
                .build();
        when(orderRepository.getReferenceById(1L)).thenReturn(entity);

        OrderDTO result = orderService.getStatus(1L);
        assertNotNull(result);
        assertEquals(OrderStatus.COMPLETED, result.orderStatus());
    }
} 