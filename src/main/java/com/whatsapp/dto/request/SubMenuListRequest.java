package com.whatsapp.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubMenuListRequest {
    private Long id;
    private String name;
    private String url;
    private Long menuId;
}
