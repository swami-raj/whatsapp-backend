package com.whatsapp.dao;

import com.whatsapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByIdAndIsActive(Long id, boolean isActive);

    Optional<Role> findByNameAndCompanyIdAndIsActive(String name, Long id, boolean isActive);

    Optional<Role> findByIdAndCompanyIdAndIsActive(Long id, Long id1, boolean isActive);

    List<Role> findByIsActive(boolean isActive);

    List<Role> findByCompanyIdAndIsActive(Long id, boolean isActive);
}
