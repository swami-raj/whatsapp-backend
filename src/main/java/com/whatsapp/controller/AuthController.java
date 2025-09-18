package com.whatsapp.controller;

import com.whatsapp.dto.request.UserDetailRequest;
import com.whatsapp.dto.request.UserLoginRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.repository.ServiceAccessor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("auth")
public class AuthController {

    @PostMapping("signup")
    public ResponseDto<UserDetailResponse> signup(@Valid @RequestBody UserDetailRequest userDetailRequest) {
        return ServiceAccessor.getAuthService().signup(userDetailRequest);
    }

    @PostMapping("login")
    public ResponseDto<UserDetailResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        return ServiceAccessor.getAuthService().login(userLoginRequest);
    }

    @GetMapping("get-all")
    public ResponseDto<List<UserDetailResponse>> getAllUsers( ) {
        return ServiceAccessor.getAuthService().getAllUsers();
    }

    @GetMapping("getById/{id}")
    public ResponseDto<UserDetailResponse> getUserById(@PathVariable Long id) {
        return ServiceAccessor.getAuthService().getUserById(id);
    }

    @DeleteMapping("deleteById/{id}")
    public ResponseDto<String> deleteUser(@PathVariable Long id) {
        return ServiceAccessor.getAuthService().deleteUser(id);
    }

}
