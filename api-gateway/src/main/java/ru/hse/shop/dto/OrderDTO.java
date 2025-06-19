package ru.hse.shop.dto;

import ru.hse.shop.model.OrderStatus;

public record OrderDTO(Long id,
    Long senderId,
    Long receiverId,
    Long transactionAmount,
    OrderStatus orderStatus) {
}
