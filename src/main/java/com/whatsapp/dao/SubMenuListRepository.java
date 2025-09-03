package com.whatsapp.dao;


import com.whatsapp.entity.SubMenuList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubMenuListRepository extends CrudRepository<SubMenuList, Long> {
    Optional<SubMenuList> findByIdAndIsActive(Long id, boolean isActive);

    Optional<SubMenuList> findByIdAndIsActive(int id, boolean isActive);

    List<SubMenuList> findByCompanyIdAndIsActive(Long companyId, boolean isActive);

    List<SubMenuList> findByMenuListIdAndCompanyIdAndIsActive(
            Long companyId, Long menuListId, boolean isActive);

    Optional<SubMenuList> findByNameAndMenuListIdAndCompanyIdAndIsActive(
            String name, Long id, Long id1, boolean isActive);

    List<SubMenuList> findByIsActive(boolean isActive);

    Optional<SubMenuList> findByIdAndCompanyIdAndIsActive(Long id, Long id1, boolean isActive);
}
