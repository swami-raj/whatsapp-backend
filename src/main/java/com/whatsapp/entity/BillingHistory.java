package com.whatsapp.entity;

import com.whatsapp.enums.Enums;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BillingHistory extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private Double amount;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private Enums.PaymentType paymentType;

}
