package com.whatsapp.dao;


import com.whatsapp.entity.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRoleRepository extends CrudRepository<Department, Long> {

    Optional<Department> findByIdAndIsActive(int departmentId, boolean isActive);

    Optional<Department> findByIdAndIsActive(Long departmentId, boolean isActive);

    Optional<Department> findByIdAndCompanyIdAndIsActive(
            Long departmentId, Long companyId, boolean isActive);

    List<Department> findByCompanyIdAndIsActive(Long companyId, boolean isActive);

    Optional<Department> findByNameAndCompanyIdAndIsActive(
            String name, Long companyId, boolean isActive);

    List<Department> findByIsActive(boolean isActive);
}
