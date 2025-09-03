package com.whatsapp.services;

import com.whatsapp.dto.request.MenuListRequest;
import com.whatsapp.dto.response.MenuListResponse;
import com.whatsapp.dto.response.ResponseDto;

import java.util.List;

public interface MenuService {

    ResponseDto<MenuListResponse> addMenu(MenuListRequest menuListRequest);

    ResponseDto<MenuListResponse> editMenu(MenuListRequest menuListRequest);

    ResponseDto<List<MenuListResponse>> getAllMenus();

    ResponseDto<MenuListResponse> getMenuById(Long id);

    ResponseDto<List<MenuListResponse>> getAllMenusWithSubmenus();
}
