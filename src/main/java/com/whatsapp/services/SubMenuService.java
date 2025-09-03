package com.whatsapp.services;

import com.whatsapp.dto.request.SubMenuListRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.SubMenuListResponse;

import java.util.List;

public interface SubMenuService {
    ResponseDto<SubMenuListResponse> addSubMenu(SubMenuListRequest subMenuListRequest);

    ResponseDto<SubMenuListResponse> editSubMenu(SubMenuListRequest subMenuListRequest);

    ResponseDto<List<SubMenuListResponse>> getAllSubMenu();

    ResponseDto<SubMenuListResponse> getSubMenuById(Long id);

    ResponseDto<List<SubMenuListResponse>> getSubmenuByMenu(Long menuId);


}
