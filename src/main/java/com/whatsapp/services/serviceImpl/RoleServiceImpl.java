package com.whatsapp.services.serviceImpl;

import com.whatsapp.dto.request.RoleRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.RoleResponse;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.Company;
import com.whatsapp.entity.Department;
import com.whatsapp.entity.Role;
import com.whatsapp.entity.User;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.services.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Override
    public ResponseDto<RoleResponse> addRole(RoleRequest roleRequest) {
        LOGGER.info("[RoleServicesImpl >> addRole] Adding new role: {}", roleRequest.getName());
        ResponseDto<RoleResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }
            Optional<Role> existingRole = RepositoryAccessor.getRoleRepository().findByNameAndCompanyIdAndIsActive(roleRequest.getName(), optionalCompany.get().getId(), true);
            if (existingRole.isPresent()) {
                response.setCode(HttpStatus.CONFLICT.value());
                response.setMessage("Role already exists.");
                return response;
            }

            Role role = Role.builder().name(roleRequest.getName()).company(optionalCompany.get()).build();
            RepositoryAccessor.getRoleRepository().save(role);
            response.setData(mapToResponse(role));
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Role added successfully");
        } catch (Exception e) {
            LOGGER.error("[RoleServicesImpl >> addRole] Exception occurred while adding role", e);
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<RoleResponse> getRoleById(Long id) {
        LOGGER.info("[RoleServicesImpl >> getRoleById] Fetching role by id: {}", id);
        ResponseDto<RoleResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }
            Optional<Role> optionalRole = RepositoryAccessor.getRoleRepository().findByIdAndCompanyIdAndIsActive(id, optionalCompany.get().getId(), true);
            if (optionalRole.isEmpty()) {
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Role not found.");
                return response;
            }

            response.setData(mapToResponse(optionalRole.get()));
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Role fetched successfully");
        } catch (Exception e) {
            LOGGER.error("[RoleServicesImpl >> getRoleById] Exception occurred while fetching role", e);
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<List<RoleResponse>> getAllRoles() {
        LOGGER.info("[RoleServicesImpl >> getAllRoles] Fetching all roles");
        ResponseDto<List<RoleResponse>> response = new ResponseDto<>();
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
                List<Role> roles = RepositoryAccessor.getRoleRepository().findByIsActive(true);
                List<RoleResponse> roleResponses = roles.stream().map(this::mapToResponse).toList();

                response.setData(roleResponses);
                response.setCode(HttpStatus.OK.value());
                response.setMessage("Roles fetched successfully");
            } else {
                List<Role> roles = RepositoryAccessor.getRoleRepository().findByCompanyIdAndIsActive(optionalCompany.get().getId(), true);
                List<RoleResponse> roleResponses = roles.stream().map(this::mapToResponse).toList();

                response.setData(roleResponses);
                response.setCode(HttpStatus.OK.value());
                response.setMessage("Roles fetched successfully");
            }

        } catch (Exception e) {
            LOGGER.error("[RoleServicesImpl >> getAllRoles] Exception occurred while fetching roles", e);
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<RoleResponse> updateRole(RoleRequest roleRequest) {
        LOGGER.info("[RoleServicesImpl >> updateRole] Updating role: {}", roleRequest.getName());
        ResponseDto<RoleResponse> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }
            Optional<Role> optionalRole = RepositoryAccessor.getRoleRepository().findByIdAndCompanyIdAndIsActive(roleRequest.getId(), optionalCompany.get().getId(), true);
            if (optionalRole.isEmpty()) {
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Role not found.");
                return response;
            }

            Role role = optionalRole.get();
            role.setName(roleRequest.getName());

            Role updatedRole = RepositoryAccessor.getRoleRepository().save(role);
            response.setData(mapToResponse(updatedRole));
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Role updated successfully");
        } catch (Exception e) {
            LOGGER.error("[RoleServicesImpl >> updateRole] Exception occurred while updating role", e);
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<String> deleteRole(Long id) {
        LOGGER.info("[RoleServicesImpl >> deleteRole] Deleting role by id: {}", id);
        ResponseDto<String> response = new ResponseDto<>();
        try {
            Optional<Role> optionalRole = RepositoryAccessor.getRoleRepository().findByIdAndIsActive(id, true);
            if (optionalRole.isPresent()) {
                Role role = optionalRole.get();
                role.setActive(false);
                RepositoryAccessor.getRoleRepository().save(role);
                response.setCode(HttpStatus.OK.value());
                response.setMessage("Role deleted successfully.");
            } else {
                response.setCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Role not found.");
            }
        } catch (Exception e) {
            LOGGER.error("[RoleServicesImpl >> deleteRole] Exception occurred while deleting role", e);
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Internal server error");
        }
        return response;
    }
    private RoleResponse mapToResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .companyName(role.getCompany().getName())
                .build();
    }
}
