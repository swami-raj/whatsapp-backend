package com.whatsapp.services;

import com.whatsapp.dto.request.DepartmentRequest;
import com.whatsapp.dto.response.DepartmentResponse;
import com.whatsapp.dto.response.ResponseDto;

import java.util.List;

public interface DepartmentService {
    ResponseDto<DepartmentResponse> addDepartment(DepartmentRequest departmentRequest);

    ResponseDto<DepartmentResponse> getDepartmentById(Long id);

    ResponseDto<List<DepartmentResponse>> getAllDepartment();

    ResponseDto<List<DepartmentResponse>> getAll();

    ResponseDto<DepartmentResponse> updateDepartment(DepartmentRequest departmentRequest);

    ResponseDto<String> deleteDepartment(Long id);
}
