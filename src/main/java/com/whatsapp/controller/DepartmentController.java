package com.whatsapp.controller;

import com.whatsapp.dto.request.DepartmentRequest;
import com.whatsapp.dto.response.DepartmentResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.repository.ServiceAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("department")
public class DepartmentController {
    @PostMapping("add")
    ResponseDto<DepartmentResponse> addDepartment(@RequestBody DepartmentRequest departmentRequest) {
        return ServiceAccessor.getDepartmentService().addDepartment(departmentRequest);
    }

    @GetMapping("get-by-id/{id}")
    ResponseDto<DepartmentResponse> getById(@PathVariable Long id) {
        return ServiceAccessor.getDepartmentService().getDepartmentById(id);
    }

    @GetMapping("get-all")
    ResponseDto<List<DepartmentResponse>> getAll() {
        return ServiceAccessor.getDepartmentService().getAllDepartment();
    }

    @GetMapping("get-all-super-admin")
    ResponseDto<List<DepartmentResponse>> getAllDepartment() {
        return ServiceAccessor.getDepartmentService().getAll();
    }

    @PutMapping("update")
    ResponseDto<DepartmentResponse> updateDepartment(@RequestBody DepartmentRequest departmentRequest) {
        return ServiceAccessor.getDepartmentService().updateDepartment(departmentRequest);
    }

    @DeleteMapping("delete/{id}")
    ResponseDto<String> deleteDepartment(@PathVariable Long id) {
        return ServiceAccessor.getDepartmentService().deleteDepartment(id);
    }

}
