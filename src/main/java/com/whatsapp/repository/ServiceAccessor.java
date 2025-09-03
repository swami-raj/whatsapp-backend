package com.whatsapp.repository;

import com.google.gson.Gson;
import com.whatsapp.security.CustomUserDetailsService;
import com.whatsapp.services.*;
import com.whatsapp.services.serviceImpl.GenericService;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ServiceAccessor {
    @Getter private static AuthService authService;
    @Getter private static ModelMapper modelMapper;
    @Getter private static Gson gson;
    @Getter private static CustomUserDetailsService customUserDetailsService;
    @Getter private static MenuService menuService;
    @Getter private static SubMenuService subMenuService;
    @Getter private static DepartmentService departmentService;
    @Getter private static RoleService roleService;
    @Getter private static CompanyService companyServices;
    @Getter private static GenericService genericService;

    ServiceAccessor(AuthService authService,
                    ModelMapper modelMapper,
                    Gson gson,
                    GenericService genericService,
                    CustomUserDetailsService customUserDetailsService,
                    MenuService menuService,
                    SubMenuService subMenuService,
//                    MenuViewRoleServices menuViewRoleServices,
                    DepartmentService departmentService,
                    RoleService roleService,
                    CompanyService companyServices
                    ) {

        ServiceAccessor.authService = authService;
        ServiceAccessor.modelMapper = modelMapper;
        ServiceAccessor.gson = gson;
        ServiceAccessor.genericService = genericService;
        ServiceAccessor.customUserDetailsService = customUserDetailsService;
        ServiceAccessor.menuService = menuService;
        ServiceAccessor.subMenuService = subMenuService;
//        ServiceAccessor.menuViewRoleServices = menuViewRoleServices;
        ServiceAccessor.departmentService = departmentService;
        ServiceAccessor.roleService = roleService;
        ServiceAccessor.companyServices = companyServices;

    }
}
