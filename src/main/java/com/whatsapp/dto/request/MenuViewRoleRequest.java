package com.whatsapp.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuViewRoleRequest {
    private Long id;
    private int departmentId;
    private Long menuListId;
    private Long subMenuListId;
}
