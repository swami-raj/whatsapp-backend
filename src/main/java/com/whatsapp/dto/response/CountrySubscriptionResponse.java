package com.whatsapp.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountrySubscriptionResponse {
    private Long id;
    private Long userId;
    private Long countryId;
    private String countryName;
    private String countryCode;
    private Long companyId;
    private String companyName;
    private Double marketingCost;
    private Double userCost;
    private Double utilityCost;
    private Double authenticationCost;
}
