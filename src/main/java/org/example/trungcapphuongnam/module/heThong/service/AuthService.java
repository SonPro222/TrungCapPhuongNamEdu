package org.example.trungcapphuongnam.module.heThong.service;


import org.example.trungcapphuongnam.module.heThong.dto.request.LoginRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}