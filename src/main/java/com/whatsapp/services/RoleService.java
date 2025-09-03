package com.whatsapp.services;

import com.whatsapp.dto.request.RoleRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    ResponseDto<RoleResponse> addRole(RoleRequest roleRequest);

    ResponseDto<RoleResponse> getRoleById(Long id);

    ResponseDto<List<RoleResponse>> getAllRoles();

    ResponseDto<RoleResponse> updateRole(RoleRequest roleRequest);

    ResponseDto<String> deleteRole(Long id);
}
