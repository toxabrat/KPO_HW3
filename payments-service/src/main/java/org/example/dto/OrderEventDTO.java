package org.example.dto;

import lombok.Data;

@Data
public class OrderEventDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long transactionAmount;
    private String status;
} 