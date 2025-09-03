package com.whatsapp.services.serviceImpl;

import com.whatsapp.dto.request.CompanyRequest;
import com.whatsapp.dto.response.CompanyResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.Company;
import com.whatsapp.entity.Department;
import com.whatsapp.entity.User;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.services.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

    @Override
    public ResponseDto<CompanyResponse> addCompany(CompanyRequest companyRequest) {
        LOGGER.info("[CompanyServiceImpl >> addCompany] Adding new company: {}", companyRequest.getName());
        ResponseDto<CompanyResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByNameAndIsActive(companyRequest.getName(), true);
            if (optionalCompany.isPresent()) {
                response.setCode(0);
                response.setMessage("company already exists.");
                return response;
            }
            Company company =
                    Company.builder()
                            .name(companyRequest.getName())
                            .email(companyRequest.getEmail())
                            .address(companyRequest.getAddress())
                            .phone(companyRequest.getPhone())
                            .build();
            company.setCreatedBy(optionalUser.get());
            RepositoryAccessor.getCompanyRepository().save(company);
            response.setData(mapToResponse(company));
            response.setCode(1);
            response.setMessage("company added successfully");
        } catch (Exception e) {
            LOGGER.error("[CompanyServiceImpl >> addCompany] Exception occurred while adding company", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<CompanyResponse> getCompanyById(Long id) {
        LOGGER.info("[CompanyServiceImpl >> getCompanyById] Fetching company by id: {}", id);
        ResponseDto<CompanyResponse> response = new ResponseDto<>();
        try {
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(id, true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("Company not found.");
                return response;
            }
            response.setData(mapToResponse(optionalCompany.get()));
            response.setCode(1);
            response.setMessage("Company fetched successfully");
        } catch (Exception e) {
            LOGGER.error("[CompanyServiceImpl >> getCompanyById] Exception occurred while fetching company", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<List<CompanyResponse>> getAllCompany() {
        LOGGER.info("[CompanyServiceImpl >> getAllCompanies] Fetching all companies");
        ResponseDto<List<CompanyResponse>> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            if (optionalUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("User not found");
                return response;
            }
            Optional<Department> optionalDepartment = RepositoryAccessor.getDepartmentRoleRepository().findByIdAndIsActive(optionalUser.get().getDepartment().getId(), true);
            if (optionalDepartment.get().getName().equals("SUPER_ADMIN")) {
                List<Company> companies = RepositoryAccessor.getCompanyRepository().findByIsActive(true);
                List<CompanyResponse> companyResponses = companies.stream().map(this::mapToResponse).toList();
                response.setData(companyResponses);
                response.setCode(1);
                response.setMessage("Companies fetched successfully");
            } else {
                Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(optionalUser.get().getCompany().getId(), true);
                if (optionalCompany.isEmpty()) {
                    response.setCode(0);
                    response.setMessage("Company not found.");
                    return response;
                }
                Company company = optionalCompany.get();
                CompanyResponse companyResponse =
                        CompanyResponse.builder()
                                .id(company.getId())
                                .phone(company.getPhone())
                                .name(company.getName())
                                .address(company.getAddress())
                                .email(company.getEmail())
                                .logo(company.getLogo())
                                .build();
                response.setData(List.of(companyResponse));
                response.setCode(1);
                response.setMessage("Company fetched successfully");
            }

        } catch (Exception e) {
            LOGGER.error("[CompanyServiceImpl >> getAllCompanies] Exception occurred while fetching companies", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<CompanyResponse> updateCompany(CompanyRequest companyRequest) {
        LOGGER.info("[CompanyServiceImpl >> updateCompany] Updating company: {}", companyRequest.getName());
        ResponseDto<CompanyResponse> response = new ResponseDto<>();
        try {
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(companyRequest.getId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("Company not found.");
                return response;
            }
            Company company = optionalCompany.get();
            company.setName(companyRequest.getName());
            company.setEmail(companyRequest.getEmail());
            company.setAddress(companyRequest.getAddress());
            company.setPhone(companyRequest.getPhone());
            RepositoryAccessor.getCompanyRepository().save(company);
            response.setData(mapToResponse(company));
            response.setCode(1);
            response.setMessage("Company updated successfully");
        } catch (Exception e) {
            LOGGER.error("[CompanyServiceImpl >> updateCompany] Exception occurred while updating company", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<String> deleteCompany(Long id) {
        LOGGER.info("[CompanyServiceImpl >> deleteCompany] Deleting company by id: {}", id);
        ResponseDto<String> response = new ResponseDto<>();
        try {
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(id, true);
            if (optionalCompany.isPresent()) {
                Company company = optionalCompany.get();
                company.setActive(false);
                RepositoryAccessor.getCompanyRepository().save(company);
                response.setCode(1);
                response.setMessage("Company deleted successfully.");
            } else {
                response.setCode(0);
                response.setMessage("Company not found.");
            }
        } catch (Exception e) {
            LOGGER.error("[CompanyServiceImpl >> deleteCompany] Exception occurred while deleting company", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    private CompanyResponse mapToResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .email(company.getEmail())
                .address(company.getAddress())
                .phone(company.getPhone())
                .logo(company.getLogo())
                .build();
    }
}
