package com.whatsapp.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountryResponse {
    private Long id;
    private String name;
    private String code;
}
