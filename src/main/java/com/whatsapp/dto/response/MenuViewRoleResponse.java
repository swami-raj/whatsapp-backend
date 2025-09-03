package com.whatsapp.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuViewRoleResponse {
    private Long id;
    private Long departmentId;
    private Long menuListId;
    private Long subMenuListId;
}
