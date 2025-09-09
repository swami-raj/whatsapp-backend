package com.whatsapp.dao;

import com.whatsapp.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country,Long> {

    Optional<Country> findByNameAndIsActive(String name, boolean isActive);

    Optional<Country> findByIdAndIsActive(Long id, boolean isActive);
}
