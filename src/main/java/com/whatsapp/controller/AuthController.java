package com.whatsapp.controller;

import com.whatsapp.dto.request.UserDetailRequest;
import com.whatsapp.dto.request.UserLoginRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.UserDetailResponse;
import com.whatsapp.repository.ServiceAccessor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
