package com.whatsapp.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuListRequest {
    private Long id;
    private String name;
    private String url;
    private String image;
}
