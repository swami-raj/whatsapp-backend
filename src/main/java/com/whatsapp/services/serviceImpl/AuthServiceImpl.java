package com.whatsapp.services.serviceImpl;

import com.whatsapp.dto.request.MenuRequest;
import com.whatsapp.dto.request.SubMenuRequest;
import com.whatsapp.dto.request.UserDetailRequest;
import com.whatsapp.dto.request.UserLoginRequest;
import com.whatsapp.dto.response.MenuListResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.SubMenuListResponse;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.*;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.services.AuthService;
import com.whatsapp.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseDto<UserDetailResponse> signup(UserDetailRequest userDetailRequest) {
        LOGGER.info("[UserServiceImpl >> signup] Starting signup process for email: {}", userDetailRequest.getEmail());
        ResponseDto<UserDetailResponse> response = new ResponseDto<>();

        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }

            if (RepositoryAccessor.getUserRepository().existsByPhoneAndIsActive(userDetailRequest.getPhone(), true)) {
                response.setCode(0);
                response.setMessage("Phone number already exists");
                return response;
            }

            if (RepositoryAccessor.getUserRepository().existsByEmailAndIsActive(userDetailRequest.getEmail(), true)) {
                response.setCode(0);
                response.setMessage("Email already exists");
                return response;
            }

            Role userRole = null;
            if (userDetailRequest.getRoleId() != null) {
                userRole = RepositoryAccessor.getRoleRepository().findByIdAndIsActive(userDetailRequest.getRoleId(), true).orElse(null);
            }
            Department department = RepositoryAccessor.getDepartmentRoleRepository().findByIdAndIsActive(userDetailRequest.getDepartmentId(), true).orElseThrow(() -> new RuntimeException("Department not found: " + userDetailRequest.getDepartmentId()));
            Company company = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailRequest.getCompanyId(), true).orElseThrow(() -> new RuntimeException("Company not found: " + userDetailRequest.getCompanyId()));
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userDetailResponse.getId(), true);

            User user = User.builder()
                            .name(userDetailRequest.getName())
                            .email(userDetailRequest.getEmail())
                            .phone(userDetailRequest.getPhone())
                            .password(userDetailRequest.getPassword())
                            .role(userRole)
                            .department(department)
                            .company(company)
                            .build();

            user.setCreatedBy(optionalUser.get());

            User savedUser = RepositoryAccessor.getUserRepository().save(user);

            UserRole userRoleEntry = UserRole.builder().role(userRole).user(savedUser).build();
            RepositoryAccessor.getUserRoleRepository().save(userRoleEntry);

            List<MenuRequest> menuList = userDetailRequest.getMenuList();
            if (menuList != null) {
                for (MenuRequest menuRequest : menuList) {
                    Long menuId = menuRequest.getId();
                    MenuList menu = RepositoryAccessor.getMenuListRepository().findByIdAndIsActive(menuId, true).orElseThrow(() -> new RuntimeException("Menu not found: " + menuId));

                    if (menuRequest.getSubMenuList() != null) {
                        for (SubMenuRequest subMenuRequest : menuRequest.getSubMenuList()) {
                            Long subMenuId = subMenuRequest.getId();
                            boolean exists = RepositoryAccessor.getMenuViewRoleRepository().existsByMenuListIdAndSubMenuListIdAndDepartmentIdAndUserIdAndIsActive(menuId, subMenuId, department.getId(), savedUser.getId(), true);
                            if (!exists) {
                                SubMenuList subMenu = RepositoryAccessor.getSubMenuListRepository().findByIdAndIsActive(subMenuId, true).orElseThrow(() -> new RuntimeException("SubMenu not found: " + subMenuId));
                                MenuViewRole menuViewRole = MenuViewRole.builder()
                                                .menuList(menu)
                                                .subMenuList(subMenu)
                                                .department(department)
                                                .user(savedUser)
                                                .build();

                                RepositoryAccessor.getMenuViewRoleRepository().save(menuViewRole);
                            }
                        }
                    }
                }
            }

            UserDetailResponse userResponse =
                    UserDetailResponse.builder()
                            .id(savedUser.getId())
                            .name(savedUser.getName())
                            .phone(savedUser.getPhone())
                            .email(savedUser.getEmail())
                            .roleId(Objects.nonNull(userRole) ? userRole.getId() : null)
                            .departmentId(department.getId())
                            .companyId(company.getId())
                            .build();
            String authToken = jwtUtil.generateToken(userResponse);
            userResponse.setToken(authToken);
            UserDetailResponse userDetailResponse1 = UserDetailResponse.builder()
                            .id(savedUser.getId())
                            .name(savedUser.getName())
                            .email(savedUser.getEmail())
                            .phone(savedUser.getPhone())
                            .departmentId(department.getId())
                            .roleId(Objects.nonNull(userRole) ? userRole.getId() : null)
                            .token(authToken)
                            .companyId(savedUser.getCompany().getId())
                            .build();
            response.setData(userDetailResponse1);
            response.setCode(1);
            response.setMessage("Signup successful");
        } catch (Exception e) {
            LOGGER.error("[UserServiceImpl >> signup] Exception occurred during signup", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<UserDetailResponse> login(UserLoginRequest userLoginRequest) {
        LOGGER.info("[UserServiceImpl >> login] Request received:");
        ResponseDto<UserDetailResponse> response = new ResponseDto<>();
        try {
            Optional<User> userOptional = RepositoryAccessor.getUserRepository().findByEmailIgnoreCaseAndIsActive(userLoginRequest.getEmail(), true);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                    response.setCode(1);
                    response.setMessage("register-message");
                    return response;
                }

                if (!user.getPassword().equals(userLoginRequest.getPassword())) {
                    response.setCode(0);
                    response.setMessage("invalid-password");
                    return response;
                }

                Optional<UserRole> optionalUserRole = RepositoryAccessor.getUserRoleRepository().findByUserIdAndIsActive(user.getId(), true);
                Long roleId = optionalUserRole.map(UserRole::getRole).map(Role::getId).orElse(null);
                String roleName = optionalUserRole.map(UserRole::getRole).map(Role::getName).orElse(null);
                List<MenuViewRole> menuViewRoles = RepositoryAccessor.getMenuViewRoleRepository().findByUserIdAndIsActive(user.getId(), true);
                List<MenuListResponse> menuListResponses;
                Map<Long, MenuListResponse> menuMap = new HashMap<>();
                for (MenuViewRole menuViewRole : menuViewRoles) {
                    Long menuId = menuViewRole.getMenuList().getId();

                    menuMap.putIfAbsent(menuId, MenuListResponse.builder()
                                    .id(menuId)
                                    .name(menuViewRole.getMenuList().getName())
                                    .url(menuViewRole.getMenuList().getUrl())
                                    .image(menuViewRole.getMenuList().getImage())
                                    .submenulist(new ArrayList<>())
                                    .build());

                    if (menuViewRole.getSubMenuList() != null) {
                        SubMenuListResponse subMenuResponse =
                                SubMenuListResponse.builder()
                                        .id(menuViewRole.getSubMenuList().getId())
                                        .name(menuViewRole.getSubMenuList().getName())
                                        .url(menuViewRole.getSubMenuList().getUrl())
                                        .menuId(menuId)
                                        .build();
                        menuMap.get(menuId).getSubmenulist().add(subMenuResponse);
                    }
                }

                menuListResponses = new ArrayList<>(menuMap.values());
                Optional<Company> company = Optional.empty();
                Long companyId1 = null;
                if (user.getCompany() != null) {
                    company = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(user.getCompany().getId(), true);
                    if (company.isPresent()) {
                        companyId1 = company.get().getId();
                    }
                }

                UserDetailResponse userDetailResponse = UserDetailResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .roleId(roleId)
                        .roleName(roleName)
                        .menulist(menuListResponses)
                        .departmentId(user.getDepartment().getId())
                        .departmentName(user.getDepartment().getName())
                        .companyId(companyId1)
                        .build();

                String authToken = jwtUtil.generateToken(userDetailResponse);
                userDetailResponse.setToken(authToken);

                response.setData(userDetailResponse);
                response.setCode(1);
                response.setMessage("login-success");
            } else {
                LOGGER.error("[UserServiceImpl >> login] User not found for email: {}", userLoginRequest.getEmail());
                response.setCode(0);
                response.setMessage("user-not-found");
            }
        } catch (Exception e) {
            LOGGER.error("[UserServiceImpl >> login] Exception occurred during login", e);
            response.setCode(0);
            response.setMessage("internal-server-error");
        }

        return response;
    }

    @Override
    public ResponseDto<List<UserDetailResponse>> getAllUsers() {
        LOGGER.error("[UserServiceImpl >> getAllUsers] get all user");
        ResponseDto<UserDetailResponse> response = new ResponseDto<>();
        try {
            List<User> userList = RepositoryAccessor.getUserRepository().findByIsActive(true);
            if (userList.isEmpty()) {
                response.setCode(0);
                response.setMessage("user not found.");
                return null;
            }
            List<UserDetailResponse> userDetailResponses = new ArrayList<>();
            for (User user : userList) {
                UserDetailResponse userDetailResponse = UserDetailResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .roleId(user.getRole() != null ? user.getRole().getId() : null)
                        .roleName(user.getRole() != null ? user.getRole().getName() : null)
                        .departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null)
                        .departmentName(user.getDepartment() != null ? user.getDepartment().getName() : null)
                        .companyId(user.getCompany() != null ? user.getCompany().getId() : null)
                        .companyName(user.getCompany() != null ? user.getCompany().getName() : null)
                        .build();
                userDetailResponses.add(userDetailResponse);
            }
            ResponseDto<List<UserDetailResponse>> responseDto = new ResponseDto<>();
            responseDto.setData(userDetailResponses);
            responseDto.setCode(1);
            responseDto.setMessage("user fetched successfully");
            return responseDto;
        } catch (Exception e) {
            LOGGER.error("[UserServiceImpl >> getAllUsers] Exception occurred while fetching user", e);
            response.setCode(0);
            response.setMessage("Internal server error");
            return null;
        }
    }

    @Override
    public ResponseDto<UserDetailResponse> getUserById(Long id) {
        LOGGER.info("[UserServiceImpl >> getUserById] Fetching user by id: {}", id);
        ResponseDto<UserDetailResponse> response = new ResponseDto<>();
        try {
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(id, true);
            if (optionalUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("user not found.");
                return response;
            }
            User user = optionalUser.get();
            UserDetailResponse userDetailResponse = UserDetailResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .roleId(user.getRole() != null ? user.getRole().getId() : null)
                    .roleName(user.getRole() != null ? user.getRole().getName() : null)
                    .departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null)
                    .departmentName(user.getDepartment() != null ? user.getDepartment().getName() : null)
                    .companyId(user.getCompany() != null ? user.getCompany().getId() : null)
                    .companyName(user.getCompany() != null ? user.getCompany().getName() : null)
                    .build();
            response.setData(userDetailResponse);
            response.setCode(1);
            response.setMessage("user fetched successfully");
        } catch (Exception e) {
            LOGGER.error("[UserServiceImpl >> getUserById] Exception occurred while fetching user", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

    @Override
    public ResponseDto<String> deleteUser(Long id) {
        LOGGER.info("[UserServiceImpl >> deleteUser] Deleting user by id: {}", id);
        ResponseDto<String> response = new ResponseDto<>();
        try {
            Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActive(id, true);
            if (optionalUser.isEmpty()) {
                response.setCode(0);
                response.setMessage("user not found.");
                return response;
            }
            User user = optionalUser.get();
            user.setActive(false);
            RepositoryAccessor.getUserRepository().save(user);
            response.setData("User deleted successfully");
            response.setCode(1);
            response.setMessage("user deleted successfully");
        } catch (Exception e) {
            LOGGER.error("[UserServiceImpl >> deleteUser] Exception occurred while deleting user", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }
        return response;
    }

}
