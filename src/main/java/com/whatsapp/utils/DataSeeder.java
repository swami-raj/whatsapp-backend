package com.whatsapp.utils;

import com.whatsapp.entity.Company;
import com.whatsapp.entity.Department;
import com.whatsapp.entity.User;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.repository.ServiceAccessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Company company = ServiceAccessor.getGenericService().seedCompany();
        Department department = ServiceAccessor.getGenericService().seedDepartment(company);
        ServiceAccessor.getGenericService().createSuperAdminUser(company, department);
        User superAdmin = RepositoryAccessor.getUserRepository().findByEmailAndIsActive("dev@gmail.com", true).get();
        ServiceAccessor.getGenericService().seedMenuStructure(company, department, superAdmin);
    }
}
