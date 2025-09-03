package com.whatsapp.services.serviceImpl;

import com.whatsapp.dto.request.SubMenuListRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.SubMenuListResponse;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.*;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.services.SubMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubMenuServiceImpl implements SubMenuService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubMenuServiceImpl.class);

    @Override
    public ResponseDto<SubMenuListResponse> addSubMenu(SubMenuListRequest subMenuListRequest) {
        LOGGER.info("[ViewAccessServiceImpl >> addViewAccess] Attempting to add view access: {}", subMenuListRequest);
        ResponseDto<SubMenuListResponse> response = new ResponseDto<>();
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
        if (optionalDepartment.isEmpty()) {
            response.setCode(0);
            response.setMessage("Department not found");
            return response;
        }

        Optional<MenuList> menuListOptional = RepositoryAccessor.getMenuListRepository().findByIdAndCompanyIdAndIsActive(subMenuListRequest.getMenuId(), optionalCompany.get().getId(), true);
        if (menuListOptional.isEmpty()) {
            LOGGER.warn("[ViewAccessServiceImpl >> addViewAccess] Menu not found with ID: {}", subMenuListRequest.getMenuId());
            response.setCode(0);
            response.setMessage("Menu not found.");
            return response;
        }
        SubMenuList subMenuList =
                SubMenuList.builder()
                        .name(subMenuListRequest.getName())
                        .url(subMenuListRequest.getUrl())
                        .menuList(menuListOptional.get())
                        .company(optionalCompany.get())
                        .build();
        subMenuList.setCreatedBy(optionalUser.get());
        subMenuList = RepositoryAccessor.getSubMenuListRepository().save(subMenuList);
        MenuViewRole menuViewRole =
                MenuViewRole.builder()
                        .subMenuList(subMenuList)
                        .menuList(menuListOptional.get())
                        .department(optionalDepartment.get())
                        .user(optionalUser.get())
                        .build();
        menuViewRole.setActive(true);
        RepositoryAccessor.getMenuViewRoleRepository().save(menuViewRole);

        LOGGER.info("[ViewAccessServiceImpl >> addViewAccess] View access added successfully with ID: {}", subMenuList.getId());
        response.setCode(1);
        response.setMessage("View access added successfully.");
        response.setData(mapToResponse(subMenuList));
        return response;
    }

    @Override
    public ResponseDto<SubMenuListResponse> editSubMenu(SubMenuListRequest subMenuListRequest) {
        LOGGER.info("[ViewAccessServiceImpl >> editViewAccess] Attempting to edit view access: {}", subMenuListRequest);
        ResponseDto<SubMenuListResponse> response = new ResponseDto<>();
        UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
        if (optionalCompany.isEmpty()) {
            response.setCode(0);
            response.setMessage("company not found");
            return response;
        }

        Optional<SubMenuList> viewAccessOptional = RepositoryAccessor.getSubMenuListRepository().findByIdAndCompanyIdAndIsActive(subMenuListRequest.getId(), optionalCompany.get().getId(), true);
        if (viewAccessOptional.isEmpty()) {
            LOGGER.warn("[ViewAccessServiceImpl >> editViewAccess] View access not found with ID: {}", subMenuListRequest.getId());
            response.setCode(0);
            response.setMessage("View access not found.");
            return response;
        }

        Optional<MenuList> menuListOptional = RepositoryAccessor.getMenuListRepository().findByIdAndCompanyIdAndIsActive(subMenuListRequest.getMenuId(), optionalCompany.get().getId(), true);
        if (menuListOptional.isEmpty()) {
            LOGGER.warn("[ViewAccessServiceImpl >> editViewAccess] Menu not found with ID: {}", subMenuListRequest.getMenuId());
            response.setCode(0);
            response.setMessage("Menu not found.");
            return response;
        }

        SubMenuList subMenuList = viewAccessOptional.get();
        subMenuList.setName(subMenuListRequest.getName());
        subMenuList.setUrl(subMenuListRequest.getUrl());
        subMenuList.setMenuList(menuListOptional.get());

        RepositoryAccessor.getSubMenuListRepository().save(subMenuList);

        LOGGER.info("[ViewAccessServiceImpl >> editViewAccess] View access updated successfully for ID: {}", subMenuList.getId());
        response.setCode(1);
        response.setMessage("View access updated successfully.");
        response.setData(mapToResponse(subMenuList));
        return response;
    }

    @Override
    public ResponseDto<List<SubMenuListResponse>> getAllSubMenu() {
        LOGGER.info("[ViewAccessServiceImpl >> getAllViewAccess] Fetching all view accesses.");
        ResponseDto<List<SubMenuListResponse>> response = new ResponseDto<>();
        UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
        if (optionalCompany.isEmpty()) {
            response.setCode(0);
            response.setMessage("company not found");
            return response;
        }

        List<SubMenuList> subMenuListList = RepositoryAccessor.getSubMenuListRepository().findByCompanyIdAndIsActive(optionalCompany.get().getId(), true);
        List<SubMenuListResponse> subMenuListRespons = subMenuListList.stream().map(this::mapToResponse).collect(Collectors.toList());

        LOGGER.info("[ViewAccessServiceImpl >> getAllViewAccess] Fetched {} view accesses.", subMenuListRespons.size());
        response.setCode(1);
        response.setMessage("All view accesses fetched successfully.");
        response.setData(subMenuListRespons);
        return response;
    }

    @Override
    public ResponseDto<SubMenuListResponse> getSubMenuById(Long id) {
        LOGGER.info("[ViewAccessServiceImpl >> getViewAccessById] Fetching view access by ID: {}", id);
        ResponseDto<SubMenuListResponse> response = new ResponseDto<>();
        UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
        if (optionalCompany.isEmpty()) {
            response.setCode(0);
            response.setMessage("company not found");
            return response;
        }
        Optional<SubMenuList> viewAccessOptional = RepositoryAccessor.getSubMenuListRepository().findByIdAndCompanyIdAndIsActive(id, optionalCompany.get().getId(), true);
        if (viewAccessOptional.isEmpty()) {
            LOGGER.warn("[ViewAccessServiceImpl >> getViewAccessById] View access not found with ID: {}", id);
            response.setCode(0);
            response.setMessage("View access not found.");
            return response;
        }

        LOGGER.info("[ViewAccessServiceImpl >> getViewAccessById] View access fetched successfully for ID: {}", id);
        response.setCode(1);
        response.setMessage("View access fetched successfully.");
        response.setData(mapToResponse(viewAccessOptional.get()));
        return response;
    }

    @Override
    public ResponseDto<List<SubMenuListResponse>> getSubmenuByMenu(Long menuId) {
        LOGGER.info("[ViewAccessServiceImpl >> getSubmenuByMenu] Fetching SubMenu by ID: {}", menuId);
        ResponseDto<List<SubMenuListResponse>> response = new ResponseDto<>();
        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }
            List<SubMenuList> subMenuListList = RepositoryAccessor.getSubMenuListRepository().findByMenuListIdAndCompanyIdAndIsActive(menuId, optionalCompany.get().getId(), true);
            List<SubMenuListResponse> subMenuListRespons = subMenuListList.stream().map(this::mapToResponse).toList();

            response.setCode(1);
            response.setMessage("All view accesses fetched successfully.");
            response.setData(subMenuListRespons);
        } catch (Exception e) {
            LOGGER.error("[ViewAccessServiceImpl >> getSubmenuByMenu] Exception occurred during fetch", e);
            response.setCode(0);
            response.setMessage("internal-server-error");
        }
        return response;
    }

    private SubMenuListResponse mapToResponse(SubMenuList subMenuList) {
        return SubMenuListResponse.builder()
                .id(subMenuList.getId())
                .name(subMenuList.getName())
                .url(subMenuList.getUrl())
                .menuId(subMenuList.getMenuList().getId())
                .build();
    }
}
