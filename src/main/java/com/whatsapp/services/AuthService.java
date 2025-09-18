package com.whatsapp.services;

import com.whatsapp.dto.request.UserDetailRequest;
import com.whatsapp.dto.request.UserLoginRequest;
import com.whatsapp.dto.response.ResponseDto;
import com.whatsapp.dto.response.UserDetailResponse;

import java.util.List;

public interface AuthService {
    ResponseDto<UserDetailResponse> signup(UserDetailRequest userDetailRequest);

    ResponseDto<UserDetailResponse> login(UserLoginRequest userLoginRequest);

    ResponseDto<List<UserDetailResponse>> getAllUsers( );

    ResponseDto<UserDetailResponse> getUserById(Long id);

    ResponseDto<String> deleteUser(Long id);
}
