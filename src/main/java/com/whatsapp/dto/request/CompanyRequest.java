package com.whatsapp.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyRequest {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
}
