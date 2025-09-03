package com.whatsapp.dao;


import com.whatsapp.entity.MenuViewRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuViewRoleRepository extends CrudRepository<MenuViewRole, Long> {

    boolean existsByMenuListIdAndSubMenuListIdAndDepartmentIdAndUserIdAndIsActive(Long menuId, Long subMenuId, Long id, Long id1, boolean isActive);

    List<MenuViewRole> findByUserIdAndIsActive(Long id, boolean isActive);

    Optional<MenuViewRole> findByMenuListIdAndSubMenuListIdAndDepartmentIdAndUserIdAndIsActive(Long id, Long id1, Long id2, Long id3, boolean isActive);
}
