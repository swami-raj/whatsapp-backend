package com.whatsapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static com.whatsapp.utils.Constants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailRequest {
    private Long id;

    @NotEmpty(message = "Name is required")
    @Pattern(regexp = NAME_REG_EXP, message = "Invalid name")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid Email", regexp = EMAIL_REG_EXP)
    private String email;

    @NotEmpty(message = "Phone number is required")
    @Size(max = 10, min = 10, message = "Phone number must be exactly 10 digits")
    @Pattern(regexp = PHONE_REG_EXP, message = "Invalid Phone number")
    private String phone;

    @NotEmpty(message = "Password is required")
    @Size(max = 15, min = 8, message = "Password must be 8 characters long")
    private String password;


    private int departmentId;
    private Long companyId;
    private Long roleId;
    private List<MenuRequest> menuList;
}
