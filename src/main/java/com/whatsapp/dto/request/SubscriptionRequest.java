package com.whatsapp.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionRequest {
    private Long id;
    private Long userId;
    private String detuctionFrom;
    private String gateway;
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
