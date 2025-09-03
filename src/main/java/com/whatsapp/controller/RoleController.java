package com.whatsapp.controller;

import com.whatsapp.dto.request.RoleRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.RoleResponse;
import com.whatsapp.repository.ServiceAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {
    @PostMapping("add")
    ResponseDto<RoleResponse> addRole(@RequestBody RoleRequest roleRequest) {
        return ServiceAccessor.getRoleService().addRole(roleRequest);
    }

    @GetMapping("get-by-id/{id}")
    ResponseDto<RoleResponse> getById(@PathVariable Long id) {
        return ServiceAccessor.getRoleService().getRoleById(id);
    }

    @GetMapping("get-all")
    ResponseDto<List<RoleResponse>> getAll() {
        return ServiceAccessor.getRoleService().getAllRoles();
    }

    @PutMapping("update")
    ResponseDto<RoleResponse> updateRole(@RequestBody RoleRequest roleRequest) {
        return ServiceAccessor.getRoleService().updateRole(roleRequest);
    }

    @DeleteMapping("delete/{id}")
    ResponseDto<String> deleteRole(@PathVariable Long id) {
        return ServiceAccessor.getRoleService().deleteRole(id);
    }
}
