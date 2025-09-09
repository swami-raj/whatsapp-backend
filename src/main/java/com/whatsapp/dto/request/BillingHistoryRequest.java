package com.whatsapp.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillingHistoryRequest {
    private Long id;
    private Long userId;
    private Double amount;
    private String paymentType;
}
