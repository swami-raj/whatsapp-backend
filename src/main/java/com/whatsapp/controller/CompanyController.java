package com.whatsapp.controller;

import com.whatsapp.dto.request.CompanyRequest;
import com.whatsapp.dto.response.CompanyResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.repository.ServiceAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("company")
public class CompanyController {
    @PostMapping("add")
    ResponseDto<CompanyResponse> addCompany(@RequestBody CompanyRequest companyRequest) {
        return ServiceAccessor.getCompanyServices().addCompany(companyRequest );
    }

    @GetMapping("get-by-id/{id}")
    ResponseDto<CompanyResponse> getById(@PathVariable Long id) {
        return ServiceAccessor.getCompanyServices().getCompanyById(id);
    }

    @GetMapping("get-all")
    ResponseDto<List<CompanyResponse>> getAll() {
        return ServiceAccessor.getCompanyServices().getAllCompany();
    }

    @PutMapping("update")
    ResponseDto<CompanyResponse> updateDepartment(@RequestBody CompanyRequest companyRequest) {
        return ServiceAccessor.getCompanyServices().updateCompany(companyRequest);
    }

    @DeleteMapping("delete/{id}")
    ResponseDto<String> deleteDepartment(@PathVariable Long id) {
        return ServiceAccessor.getCompanyServices().deleteCompany(id);
    }
}
