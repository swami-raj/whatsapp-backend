package com.whatsapp.controller;

import com.whatsapp.dto.request.SubMenuListRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.SubMenuListResponse;
import com.whatsapp.repository.ServiceAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sub-menu")
public class SubMenuController {
    @PostMapping("add")
    public ResponseDto<SubMenuListResponse> addSubMenu(@RequestBody SubMenuListRequest subMenuListRequest) {
        return ServiceAccessor.getSubMenuService().addSubMenu(subMenuListRequest);
    }

    @PutMapping("edit/{id}")
    public ResponseDto<SubMenuListResponse> editSubMenu(@RequestBody SubMenuListRequest subMenuListRequest) {
        return ServiceAccessor.getSubMenuService().editSubMenu(subMenuListRequest);
    }

    @GetMapping("get-all")
    public ResponseDto<List<SubMenuListResponse>> getAllSubMenu() {
        return ServiceAccessor.getSubMenuService().getAllSubMenu();
    }

    @GetMapping("{id}")
    public ResponseDto<SubMenuListResponse> getSubMenuById(@PathVariable Long id) {
        return ServiceAccessor.getSubMenuService().getSubMenuById(id);
    }

    @GetMapping("get-subMenu-by-menu")
    public ResponseDto<List<SubMenuListResponse>> getSubmenuByMenu(@RequestParam Long menuId) {
        return ServiceAccessor.getSubMenuService().getSubmenuByMenu(menuId);
    }
}
