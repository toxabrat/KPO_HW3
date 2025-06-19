package ru.hse.shop.dto;

import lombok.Data;

@Data
public class PaymentResultDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long transactionAmount;
    private String status;
    private String eventType;
}