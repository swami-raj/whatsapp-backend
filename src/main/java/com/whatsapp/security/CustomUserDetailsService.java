package com.whatsapp.security;


import com.whatsapp.entity.Department;
import com.whatsapp.entity.User;
import com.whatsapp.repository.RepositoryAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional =
                RepositoryAccessor.getUserRepository().findByEmailIgnoreCase(email);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        User user = userOptional.get();

        Department department =
                RepositoryAccessor.getDepartmentRoleRepository()
                        .findByIdAndIsActive(user.getDepartment().getId(), true)
                        .orElseThrow(
                                () -> new RuntimeException("department not found for user: " + user.getId()));

        return new CustomUserDetails(user, department);
    }
}
