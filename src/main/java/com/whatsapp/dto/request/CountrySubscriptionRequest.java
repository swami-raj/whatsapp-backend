package com.whatsapp.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountrySubscriptionRequest {
    private Long id;
    private Long userId;
    private Long countryId;
    private Double marketingCost;
    private Double userCost;
    private Double utilityCost;
    private Double authenticationCost;

}
