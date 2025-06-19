package ru.hse.shop.dto;

public record OrderCreateDTO(Long senderId,
    Long receiverId,
    Long transactionAmount) {
}
