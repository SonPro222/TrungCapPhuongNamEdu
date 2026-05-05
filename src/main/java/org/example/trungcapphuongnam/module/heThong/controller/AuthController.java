package org.example.trungcapphuongnam.module.heThong.controller;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.LoginRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.LoginResponse;
import org.example.trungcapphuongnam.module.heThong.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(authService.login(request))
        );
    }
}