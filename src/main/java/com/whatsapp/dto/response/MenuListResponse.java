package com.whatsapp.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuListResponse {
    private Long id;
    private String name;
    private String url;
    private String image;
    private String isActive;
    private List<SubMenuListResponse> submenulist;
}
