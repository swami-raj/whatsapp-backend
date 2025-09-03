package com.whatsapp.services.serviceImpl;

import com.whatsapp.dto.request.DepartmentRequest;
import com.whatsapp.dto.response.DepartmentResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.Company;
import com.whatsapp.entity.Department;
import com.whatsapp.entity.User;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.services.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    @Override
    public ResponseDto<DepartmentResponse> addDepartment(DepartmentRequest departmentRequest) {
        LOGGER.info("[DepartmentServicesImpl >> addDepartment] Adding new permission: {}", departmentRequest.getName());
        ResponseDto<DepartmentResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }
            Optional<Department> existingPermission = RepositoryAccessor.getDepartmentRoleRepository().findByNameAndCompanyIdAndIsActive(departmentRequest.getName(), optionalCompany.get().getId(), true);
            if (existingPermission.isPresent()) {
                response.setCode(0);
                response.setMessage("Permission already exists.");
                return response;
            }

            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            if (optionalUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("User not found");
                return response;
            }

            Department department = Department.builder()
                            .name(departmentRequest.getName())
                            .company(optionalCompany.get())
                            .build();
            department.setCreatedBy(optionalUser.get());
            RepositoryAccessor.getDepartmentRoleRepository().save(department);

            response.setData(mapToResponse(department));
            response.setCode(1);
            response.setMessage("department added successfully");
        } catch (Exception e) {
            LOGGER.error("[DepartmentServicesImpl >> addDepartment] Exception occurred while adding department", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<DepartmentResponse> getDepartmentById(Long id) {
        LOGGER.info("[DepartmentServicesImpl >> getById] Fetching department by id: {}", id);
        ResponseDto<DepartmentResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }
            Optional<Department> optionalDepartment = RepositoryAccessor.getDepartmentRoleRepository().findByIdAndCompanyIdAndIsActive(id, optionalCompany.get().getId(), true);
            if (optionalDepartment.isEmpty()) {
                response.setCode(0);
                response.setMessage("department not found.");
                return response;
            }

            response.setData(mapToResponse(optionalDepartment.get()));
            response.setCode(1);
            response.setMessage("department fetched successfully");
        } catch (Exception e) {
            LOGGER.error("[DepartmentServicesImpl >> getById] Exception occurred while fetching department", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<List<DepartmentResponse>> getAllDepartment() {
        LOGGER.info("[DepartmentServicesImpl >> getAll] Fetching all department");
        ResponseDto<List<DepartmentResponse>> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            if (optionalUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("User not found");
                return response;
            }
            Optional<Department> optionalDepartment = RepositoryAccessor.getDepartmentRoleRepository().findByIdAndIsActive(optionalUser.get().getDepartment().getId(), true);
            if (optionalDepartment.get().getName().equals("SUPER_ADMIN")) {
                List<Department> departments = RepositoryAccessor.getDepartmentRoleRepository().findByIsActive(true);
                List<DepartmentResponse> departmentResponses = departments.stream().map(this::mapToResponse).toList();

                response.setData(departmentResponses);
                response.setCode(1);
                response.setMessage("department fetched successfully");
            } else {
                List<Department> departments = RepositoryAccessor.getDepartmentRoleRepository().findByCompanyIdAndIsActive(optionalCompany.get().getId(), true);
                List<DepartmentResponse> departmentResponses = departments.stream().map(this::mapToResponse).toList();

                response.setData(departmentResponses);
                response.setCode(1);
                response.setMessage("department fetched successfully");
            }

        } catch (Exception e) {
            LOGGER.error("[DepartmentServicesImpl >> getAll] Exception occurred while fetching department", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<List<DepartmentResponse>> getAll() {
        LOGGER.info("[DepartmentServicesImpl >> getAllD] Fetching all department");
        ResponseDto<List<DepartmentResponse>> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);
            if (optionalUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("User not found");
                return response;
            }
            Optional<Department> optionalDepartment = RepositoryAccessor.getDepartmentRoleRepository().findByIdAndIsActive(optionalUser.get().getDepartment().getId(), true);
            if (optionalDepartment.get().getName().equals("SUPER_ADMIN")) {
                List<Department> departments = RepositoryAccessor.getDepartmentRoleRepository().findByCompanyIdAndIsActive(optionalUser.get().getCompany().getId(), true);
                List<DepartmentResponse> departmentResponses = departments.stream().map(this::mapToResponse).toList();

                response.setData(departmentResponses);
                response.setCode(1);
                response.setMessage("department fetched successfully");
            }

        } catch (Exception e) {
            LOGGER.error("[DepartmentServicesImpl >> getAll] Exception occurred while fetching department", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<DepartmentResponse> updateDepartment(DepartmentRequest departmentRequest) {
        LOGGER.info("[DepartmentServicesImpl >> updateDepartment] Editing permission: {}", departmentRequest.getName());
        ResponseDto<DepartmentResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }
            Optional<Department> optionalDepartment = RepositoryAccessor.getDepartmentRoleRepository().findByIdAndCompanyIdAndIsActive(departmentRequest.getId(), optionalCompany.get().getId(), true);
            if (optionalDepartment.isEmpty()) {
                response.setCode(0);
                response.setMessage("department not found.");
                return response;
            }
            Department department = optionalDepartment.get();
            department.setName(departmentRequest.getName());
            Department updatedDepartment = RepositoryAccessor.getDepartmentRoleRepository().save(department);

            response.setData(mapToResponse(updatedDepartment));
            response.setCode(1);
            response.setMessage("department updated successfully");

        } catch (Exception e) {
            LOGGER.error("[DepartmentServicesImpl >> editDepartment] Exception occurred while editing department", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<String> deleteDepartment(Long id) {
        LOGGER.info("[DepartmentServicesImpl >> delete] Delete department by id: {}", id);
        ResponseDto<String> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }
            Optional<Department> optionalDepartment = RepositoryAccessor.getDepartmentRoleRepository().findByIdAndCompanyIdAndIsActive(id, optionalCompany.get().getId(), true);
            if (optionalDepartment.isPresent()) {
                Department department = optionalDepartment.get();
                department.setActive(false);
                RepositoryAccessor.getDepartmentRoleRepository().save(department);
                response.setCode(1);
                response.setMessage("department deleted successfully.");
            } else {
                response.setCode(0);
                response.setMessage("department not found.");
            }

        } catch (Exception e) {
            LOGGER.error(
                    "[DepartmentServicesImpl >> delete] Exception occurred while delete department", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }
    private DepartmentResponse mapToResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .companyName(department.getCompany().getName())
                .build();
    }
}
