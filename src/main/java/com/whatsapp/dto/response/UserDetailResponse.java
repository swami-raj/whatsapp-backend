package com.whatsapp.dto.response;

import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private Long departmentId;
    private String departmentName;
    private Long roleId;
    private String roleName;
    private String token;
    private List<MenuListResponse> menulist;
    private Long companyId;
    private String companyName;
}
