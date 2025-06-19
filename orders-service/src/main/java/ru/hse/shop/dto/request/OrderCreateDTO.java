package ru.hse.shop.dto.request;

import ru.hse.shop.model.OrderStatus;

public record OrderCreateDTO(Long senderId,
    Long receiverId,
    Long transactionAmount) {
}
