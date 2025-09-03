package com.whatsapp.controller;

import com.whatsapp.dto.request.MenuListRequest;
import com.whatsapp.dto.response.MenuListResponse;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.repository.ServiceAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("menu")
public class MenuController {
    @PostMapping("add")
    public ResponseDto<MenuListResponse> addMenu(@RequestBody MenuListRequest menuListRequest) {
        return ServiceAccessor.getMenuService().addMenu(menuListRequest);
    }

    @PutMapping("edit/{id}")
    public ResponseDto<MenuListResponse> editMenu(@RequestBody MenuListRequest menuListRequest) {
        return ServiceAccessor.getMenuService().editMenu(menuListRequest);
    }

    @GetMapping("get-all")
    public ResponseDto<List<MenuListResponse>> getAllMenus() {
        return ServiceAccessor.getMenuService().getAllMenus();
    }

    @GetMapping("{id}")
    public ResponseDto<MenuListResponse> getMenuById(@PathVariable Long id) {
        return ServiceAccessor.getMenuService().getMenuById(id);
    }

    @GetMapping("get-all-menus-with-submenus")
    public ResponseDto<List<MenuListResponse>> getAllMenusWithSubmenus() {
        return ServiceAccessor.getMenuService().getAllMenusWithSubmenus();
    }
}
