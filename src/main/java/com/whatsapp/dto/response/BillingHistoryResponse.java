package com.whatsapp.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingHistoryResponse {
    private Long id;
    private Long userId;
    private Double amount;
    private String paymentType;
    private String transactionId;
    private String userName;
}
