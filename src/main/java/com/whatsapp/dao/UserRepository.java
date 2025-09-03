package com.whatsapp.dao;

import com.whatsapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByEmailAndIsActive(String username, boolean isActive);

    Optional<User> findByIdAndIsActive(Long id, boolean isActive);

    boolean existsByPhoneAndIsActive( String phone, boolean isActive);

    boolean existsByEmailAndIsActive(String email, boolean isActive);

    Optional<User> findByEmailIgnoreCaseAndIsActive( String email, boolean isActive);
}
