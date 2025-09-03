package com.whatsapp.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubMenuListResponse {
    private Long id;
    private String name;
    private String url;
    private String isActive;
    private Long menuId;
}
