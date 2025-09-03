package com.whatsapp.services;

import com.whatsapp.dto.request.CompanyRequest;
import com.whatsapp.dto.response.CompanyResponse;
import com.whatsapp.dto.response.ResponseDto;

import java.util.List;

public interface CompanyService {
    ResponseDto<CompanyResponse> addCompany(CompanyRequest companyRequest);

    ResponseDto<CompanyResponse> getCompanyById(Long id);

    ResponseDto<List<CompanyResponse>> getAllCompany();

    ResponseDto<CompanyResponse> updateCompany(CompanyRequest companyRequest);

    ResponseDto<String> deleteCompany(Long id);
}
