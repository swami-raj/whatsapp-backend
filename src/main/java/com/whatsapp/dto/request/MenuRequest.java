package com.whatsapp.dto.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuRequest {
    private Long id;
    private List<SubMenuRequest> subMenuList;
}
