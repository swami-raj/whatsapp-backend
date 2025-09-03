package com.whatsapp.repository;

import com.whatsapp.dao.*;


import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class RepositoryAccessor {
    @Getter private static UserRepository userRepository;
    @Getter private static RoleRepository roleRepository;
    @Getter private static UserRoleRepository userRoleRepository;
    @Getter private static CompanyRepository companyRepository;
    @Getter private static MenuListRepository menuListRepository;
    @Getter private static SubMenuListRepository subMenuListRepository;
    @Getter private static MenuViewRoleRepository menuViewRoleRepository;
    @Getter private static UserTokenRepository userTokenRepository;
    @Getter private static DepartmentRoleRepository departmentRoleRepository;

    RepositoryAccessor(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository,
                       CompanyRepository companyRepository, MenuListRepository menuListRepository, SubMenuListRepository subMenuListRepository,
                       MenuViewRoleRepository menuViewRoleRepository, UserTokenRepository userTokenRepository, DepartmentRoleRepository departmentRoleRepository) {
        RepositoryAccessor.departmentRoleRepository = departmentRoleRepository;
        RepositoryAccessor.userRepository = userRepository;
        RepositoryAccessor.roleRepository = roleRepository;
        RepositoryAccessor.userRoleRepository = userRoleRepository;
        RepositoryAccessor.companyRepository = companyRepository;
        RepositoryAccessor.menuListRepository = menuListRepository;
        RepositoryAccessor.subMenuListRepository = subMenuListRepository;
        RepositoryAccessor.menuViewRoleRepository = menuViewRoleRepository;
        RepositoryAccessor.userTokenRepository = userTokenRepository;
    }

}
