package ru.hse.shop.dto.response;

import ru.hse.shop.model.OrderStatus;

public record OrderDTO(Long id,
                       Long senderId,
                       Long receiverId,
                       Long transactionAmount,
                       OrderStatus orderStatus) { }
