package com.whatsapp.services.serviceImpl;

import com.whatsapp.dto.request.MenuListRequest;
import com.whatsapp.dto.response.MenuListResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.SubMenuListResponse;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.Company;
import com.whatsapp.entity.MenuList;
import com.whatsapp.entity.SubMenuList;
import com.whatsapp.entity.User;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.services.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Override
    public ResponseDto<MenuListResponse> addMenu(MenuListRequest menuListRequest) {
        LOGGER.info("[MenuImpl >> addMenu] Adding new menu: {}", menuListRequest.getName());
        ResponseDto<MenuListResponse> response = new ResponseDto<>();
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
            MenuList menuList = MenuList.builder()
                            .name(menuListRequest.getName())
                            .url(menuListRequest.getUrl())
                            .image(menuListRequest.getImage())
                            .company(optionalCompany.get())
                            .build();
            menuList.setCreatedBy(optionalUser.get());
            MenuList savedMenu = RepositoryAccessor.getMenuListRepository().save(menuList);

            response.setData(mapToResponse(savedMenu));
            response.setCode(1);
            response.setMessage("Menu added successfully");
        } catch (Exception e) {
            LOGGER.error("[MenuImpl >> addMenu] Exception occurred while adding menu", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }

        return response;
    }

    @Override
    public ResponseDto<MenuListResponse> editMenu(MenuListRequest menuListRequest) {
        LOGGER.info("[MenuImpl >> editMenu] Editing menu ID: {}", menuListRequest.getId());
        ResponseDto<MenuListResponse> response = new ResponseDto<>();

        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }

            Optional<MenuList> optionalMenu = RepositoryAccessor.getMenuListRepository().findByIdAndCompanyIdAndIsActive(menuListRequest.getId(), optionalCompany.get().getId(), true);
            if (optionalMenu.isEmpty()) {
                response.setCode(0);
                response.setMessage("Menu not found");
                return response;
            }

            MenuList menuList = optionalMenu.get();
            menuList.setName(menuListRequest.getName());
            menuList.setUrl(menuListRequest.getUrl());
            menuList.setImage(menuListRequest.getImage());
            MenuList updatedMenu = RepositoryAccessor.getMenuListRepository().save(menuList);

            response.setData(mapToResponse(updatedMenu));
            response.setCode(1);
            response.setMessage("Menu updated successfully");
        } catch (Exception e) {
            LOGGER.error("[MenuImpl >> editMenu] Exception occurred while editing menu", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }

        return response;
    }

    @Override
    public ResponseDto<List<MenuListResponse>> getAllMenus() {
        LOGGER.info("[MenuImpl >> getAllMenus] Fetching all menus");
        ResponseDto<List<MenuListResponse>> response = new ResponseDto<>();

        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }

            List<MenuList> menus = RepositoryAccessor.getMenuListRepository().findByCompanyIdAndIsActive(optionalCompany.get().getId(), true);
            List<MenuListResponse> menuResponses = menus.stream().map(this::mapToResponse).collect(Collectors.toList());

            response.setData(menuResponses);
            response.setCode(1);
            response.setMessage("Menu list retrieved successfully");
        } catch (Exception e) {
            LOGGER.error("[MenuImpl >> getAllMenus] Exception occurred while fetching menus", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }

        return response;
    }

    @Override
    public ResponseDto<MenuListResponse> getMenuById(Long id) {
        LOGGER.info("[MenuImpl >> getMenuById] Fetching menu ID: {}", id);
        ResponseDto<MenuListResponse> response = new ResponseDto<>();

        try {
            UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
            if (optionalCompany.isEmpty()) {
                response.setCode(0);
                response.setMessage("company not found");
                return response;
            }

            MenuList menu = RepositoryAccessor.getMenuListRepository().findByIdAndCompanyIdAndIsActive(id, optionalCompany.get().getId(), true).orElseThrow(() -> new RuntimeException("Menu not found"));

            response.setData(mapToResponse(menu));
            response.setCode(1);
            response.setMessage("Menu retrieved successfully");
        } catch (Exception e) {
            LOGGER.error("[MenuImpl >> getMenuById] Exception occurred while fetching menu", e);
            response.setCode(0);
            response.setMessage("Internal server error");
        }

        return response;
    }

    @Override
    public ResponseDto<List<MenuListResponse>> getAllMenusWithSubmenus() {
        LOGGER.info("[MenuListServiceImpl >> getAllMenusWithSubmenus] Fetching all menus with submenus.");

        ResponseDto<List<MenuListResponse>> response = new ResponseDto<>();
        UserDetailResponse userDetailResponse = (UserDetailResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByIdAndIsActive(userDetailResponse.getCompanyId(), true);
        if (optionalCompany.isEmpty()) {
            response.setCode(0);
            response.setMessage("company not found");
            return response;
        }

        List<MenuList> menus = RepositoryAccessor.getMenuListRepository().findByIsActive(true);
        List<SubMenuList> submenus = RepositoryAccessor.getSubMenuListRepository().findByIsActive(true);
        Map<Long, List<SubMenuListResponse>> subMenuMap = submenus.stream().map(this::mapToSubMenuResponse).collect(Collectors.groupingBy(SubMenuListResponse::getMenuId));
        List<MenuListResponse> menuResponses = menus.stream().map(menu -> {List<SubMenuListResponse> submenuList = subMenuMap.getOrDefault(menu.getId(), new ArrayList<>());
            return mapToMenuResponse(menu, submenuList);
        }).collect(Collectors.toList());

        response.setCode(1);
        response.setMessage("Menus and submenus retrieved successfully.");
        response.setData(menuResponses);

        return response;
    }
    private MenuListResponse mapToMenuResponse(
            MenuList menuList, List<SubMenuListResponse> subMenuList) {
        return MenuListResponse.builder()
                .id(menuList.getId())
                .name(menuList.getName())
                .url(menuList.getUrl())
                .image(menuList.getImage())
                .submenulist(subMenuList)
                .build();
    }

    private SubMenuListResponse mapToSubMenuResponse(SubMenuList subMenuList) {
        return SubMenuListResponse.builder()
                .id(subMenuList.getId())
                .name(subMenuList.getName())
                .url(subMenuList.getUrl())
                .menuId(subMenuList.getMenuList().getId())
                .build();
    }

    private MenuListResponse mapToResponse(MenuList menuList) {
        return MenuListResponse.builder()
                .id(menuList.getId())
                .name(menuList.getName())
                .url(menuList.getUrl())
                .image(menuList.getImage())
                .build();
    }
}
