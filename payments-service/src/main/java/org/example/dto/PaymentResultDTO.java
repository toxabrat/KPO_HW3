package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResultDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long transactionAmount;
    private String status;
    private String eventType;
} 