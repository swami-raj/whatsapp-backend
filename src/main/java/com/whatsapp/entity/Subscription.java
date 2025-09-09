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
public class Subscription extends BaseModel{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Enumerated(EnumType.STRING)
    private Enums.DetuctionFrom detuctionFrom;

    @Enumerated(EnumType.STRING)
    private Enums.Gateway gateway;

    private boolean webhook;
    private String webhookUrl;
    private boolean webhookHeaderEnable;
    private String webhookHeaderValue;
    private String cloudApiToken;
    private String whatsappNumber;
    private String cloudAppId;
    private String cloudWabaId;
    private String cloudPhoneNumberId;
    private int maxCampSize;
    private long expiryDate;
}
