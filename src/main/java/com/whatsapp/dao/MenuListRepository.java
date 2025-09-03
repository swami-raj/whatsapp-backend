package com.whatsapp.dao;


import com.whatsapp.entity.MenuList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuListRepository extends CrudRepository<MenuList, Long> {

    Optional<MenuList> findByIdAndIsActive(Long id, boolean isActive);

    Optional<MenuList> findByIdAndIsActive(int id, boolean isActive);

    List<MenuList> findByCompanyIdAndIsActive(Long companyId, boolean isActive);

    Optional<MenuList> findByNameAndCompanyIdAndIsActive(String menu, Long id, boolean isActive);

    List<MenuList> findByIsActive(boolean isActive);

    Optional<MenuList> findByIdAndCompanyIdAndIsActive(Long id, Long id1, boolean isActive);
}
