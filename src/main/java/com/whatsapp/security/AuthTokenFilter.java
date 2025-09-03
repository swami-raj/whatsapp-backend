package com.whatsapp.security;


import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.entity.Department;
import com.whatsapp.entity.User;
import com.whatsapp.repository.RepositoryAccessor;
import com.whatsapp.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Autowired private JwtUtil jwtUtil;

    @Autowired private UserDetailsService userServices;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        userEmail = jwtUtil.extractUserName(jwt);

        if (StringUtils.isNotEmpty(userEmail)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userServices.loadUserByUsername(userEmail);
            UserDetailResponse userResponse = jwtUtil.getUserAuthDetailsFromToken(jwt);

            Optional<User> userExists = RepositoryAccessor.getUserRepository().findByIdAndIsActive(userResponse.getId(), true);

            if (userExists.isPresent()) {
                User user = userExists.get();
                Optional<Department> optionalDepartment =
                        RepositoryAccessor.getDepartmentRoleRepository()
                                .findByIdAndIsActive(user.getDepartment().getId(), true);

                if (jwtUtil.validateToken(jwt)) {
                    UserDetailResponse userDetailResponse =
                            UserDetailResponse.builder()
                                    .id(user.getId())
                                    .email(user.getEmail())
                                    .phone(user.getPhone())
                                    .name(user.getName())
                                    .companyId(user.getCompany().getId())
                                    .build();

                    // Assuming UserRole contains the role information
                    if (optionalDepartment.isPresent()) {
                        Department department = optionalDepartment.get();
                        userDetailResponse.setDepartmentId(department.getId());
                    }

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetailResponse, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
