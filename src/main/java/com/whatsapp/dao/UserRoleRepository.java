package com.whatsapp.dao;

import com.whatsapp.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository <UserRole,Long>{

    Optional<UserRole> findByUserIdAndIsActive(Long id, boolean isActive);
}
