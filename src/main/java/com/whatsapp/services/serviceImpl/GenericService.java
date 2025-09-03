package com.whatsapp.services.serviceImpl;

import com.google.gson.Gson;
import com.whatsapp.entity.*;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.repository.ServiceAccessor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GenericService {
    private final Logger LOGGER = LoggerFactory.getLogger(GenericService.class);

    @Autowired private Gson gson;
    private final ModelMapper modelMapper = ServiceAccessor.getModelMapper();
    @Autowired private PasswordEncoder bcryptEncoder;

    @Transactional
    public Company seedCompany() {
        Optional<Company> optionalCompany = RepositoryAccessor.getCompanyRepository().findByEmailAndIsActive("superadmin@radiancesolutions.com", true);
        if (optionalCompany.isPresent()) {
            return optionalCompany.get();
        }
        Company company =
                Company.builder()
                        .name("Radiance Solutions")
                        .email("superadmin@radiancesolutions.com")
                        .phone("9876543210")
                        .address("Delhi")
                        .logo("logo.png")
                        .city("Delhi")
                        .pinCode("110001")
                        .website("www.radiancesolutions.com")
                        .state("Delhi")
                        .build();
        company.setActive(true);
        return RepositoryAccessor.getCompanyRepository().save(company);
    }

    @Transactional
    public Department seedDepartment(Company company) {
        List<String> departmentNames = List.of("SUPER_ADMIN", "ADMIN");
        List<Department> existingDepartments = RepositoryAccessor.getDepartmentRoleRepository().findByCompanyIdAndIsActive(company.getId(), true);
        List<String> existingNames = existingDepartments.stream().map(Department::getName).toList();
        List<Department> toSave = new ArrayList<>();
        for (String name : departmentNames) {
            if (!existingNames.contains(name)) {
                Department department = Department.builder().name(name).company(company).build();
                department.setActive(true);
                toSave.add(department);
            }
        }
        if (!toSave.isEmpty()) {
            RepositoryAccessor.getDepartmentRoleRepository().saveAll(toSave);
        }
        return RepositoryAccessor.getDepartmentRoleRepository().findByNameAndCompanyIdAndIsActive("SUPER_ADMIN", company.getId(), true).orElse(null);
    }

    @Transactional
    public User createSuperAdminUser(Company company, Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department is null while creating Super Admin User");
        }
        Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByEmailAndIsActive("dev@gmail.com", true);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        User user = new User();
        user.setEmail("dev@gmail.com");
        user.setPassword("Dev@2025");
        user.setName("dev");
        user.setPhone("9876543210");
        user.setActive(true);
        user.setCompany(company);
        user.setDepartment(department);
        RepositoryAccessor.getUserRepository().save(user);
        return user;
    }

    @Transactional
    public void seedMenuStructure(Company company, Department department, User user) {
        if (department == null || user == null) {
            System.out.println("Department or User is null. Skipping menu seeding.");
            return;
        }

        List<MenuList> menuLists = new ArrayList<>();

        MenuList menu =
                RepositoryAccessor.getMenuListRepository().findByNameAndCompanyIdAndIsActive("Menu", company.getId(), true)
                        .orElseGet(
                                () -> {
                                    MenuList m =
                                            MenuList.builder()
                                                    .name("Menu")
                                                    .url("menu-list")
                                                    .image("fas fa-bars")
                                                    .company(company)
                                                    .build();
                                    return RepositoryAccessor.getMenuListRepository().save(m);
                                });

        MenuList subMenu =
                RepositoryAccessor.getMenuListRepository()
                        .findByNameAndCompanyIdAndIsActive("Sub Menu", company.getId(), true)
                        .orElseGet(
                                () -> {
                                    MenuList sm =
                                            MenuList.builder()
                                                    .name("Sub Menu")
                                                    .url("sub-menu-list")
                                                    .image("fas fa-ellipsis-v")
                                                    .company(company)
                                                    .build();
                                    sm.setActive(true);
                                    return RepositoryAccessor.getMenuListRepository().save(sm);
                                });

        menuLists.add(menu);
        menuLists.add(subMenu);

        List<SubMenuList> subMenus =
                List.of(
                        createSubMenuIfNotExist("Add Menu", "add", menu, company),
                        createSubMenuIfNotExist("Show Menu", "show", menu, company),
                        createSubMenuIfNotExist("Add SubMenu", "add", subMenu, company),
                        createSubMenuIfNotExist("Show SubMenu", "show", subMenu, company));

        for (MenuList menuList : menuLists) {
            List<SubMenuList> associatedSubMenus = subMenus.stream().filter(s -> s.getMenuList().getId().equals(menuList.getId())).toList();

            for (SubMenuList subMenuList : associatedSubMenus) {
                Optional<MenuViewRole> existing = RepositoryAccessor.getMenuViewRoleRepository().findByMenuListIdAndSubMenuListIdAndDepartmentIdAndUserIdAndIsActive(menuList.getId(), subMenuList.getId(), department.getId(), user.getId(), true);
                if (existing.isEmpty()) {
                    MenuViewRole viewRole =
                            MenuViewRole.builder()
                                    .menuList(menuList)
                                    .subMenuList(subMenuList)
                                    .department(department)
                                    .user(user)
                                    .build();
                    viewRole.setActive(true);
                    RepositoryAccessor.getMenuViewRoleRepository().save(viewRole);
                }
            }
        }
    }
    private SubMenuList createSubMenuIfNotExist(
            String name, String url, MenuList menu, Company company) {
        return RepositoryAccessor.getSubMenuListRepository()
                .findByNameAndMenuListIdAndCompanyIdAndIsActive(name, menu.getId(), company.getId(), true)
                .orElseGet(
                        () -> {
                            SubMenuList submenu =
                                    SubMenuList.builder()
                                            .name(name)
                                            .url(url)
                                            .menuList(menu)
                                            .company(company)
                                            .build();
                            submenu.setActive(true);
                            return RepositoryAccessor.getSubMenuListRepository().save(submenu);
                        });
    }
}
