package com.whatsapp.repository;

import com.google.gson.Gson;
import com.whatsapp.dao.BillingHistoryRepository;
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
    @Getter private static CountryService countryService;
    @Getter private static CountrySubscriptionService countrySubscriptionService;
    @Getter private static BillingHistoryService billingHistoryService;
    @Getter private static SubscriptionService subscriptionService;
//    @Getter private static CloudTemplateService cloudTemplateService;
    @Getter private static CandidatesService candidatesService;

    ServiceAccessor(AuthService authService,
                    ModelMapper modelMapper,
                    Gson gson,
                    GenericService genericService,
                    CustomUserDetailsService customUserDetailsService,
                    MenuService menuService,
                    SubMenuService subMenuService,
                    DepartmentService departmentService,
                    RoleService roleService,
                    CompanyService companyServices,
                    CountryService countryService,
                    CountrySubscriptionService countrySubscriptionService,
                    BillingHistoryService billingHistoryService,
                    SubscriptionService subscriptionService,
//                    CloudTemplateService cloudTemplateService,
                    CandidatesService candidatesService
                    ) {

        ServiceAccessor.authService = authService;
        ServiceAccessor.modelMapper = modelMapper;
        ServiceAccessor.gson = gson;
        ServiceAccessor.genericService = genericService;
        ServiceAccessor.customUserDetailsService = customUserDetailsService;
        ServiceAccessor.menuService = menuService;
        ServiceAccessor.subMenuService = subMenuService;
        ServiceAccessor.departmentService = departmentService;
        ServiceAccessor.roleService = roleService;
        ServiceAccessor.companyServices = companyServices;
        ServiceAccessor.countryService = countryService;
        ServiceAccessor.countrySubscriptionService = countrySubscriptionService;
        ServiceAccessor.billingHistoryService = billingHistoryService;
       ServiceAccessor.subscriptionService = subscriptionService;
//        ServiceAccessor.cloudTemplateService = cloudTemplateService;
        ServiceAccessor.candidatesService = candidatesService;

    }
}
