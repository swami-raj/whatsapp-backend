package com.whatsapp.dao;

import com.whatsapp.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    Optional<Company> findByIdAndIsActive(Long companyId, boolean isActive);

    Optional<Company> findByNameAndIsActive(String name, boolean isActive);

    List<Company> findByIsActive(boolean isActive);

    Optional<Company> findByEmailAndIsActive(String mail, boolean IsActive);
}
