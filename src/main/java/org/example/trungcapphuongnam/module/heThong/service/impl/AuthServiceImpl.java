package org.example.trungcapphuongnam.module.heThong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.enums.TrangThaiTaiKhoan;
import org.example.trungcapphuongnam.config.jwt.JwtService;
import org.example.trungcapphuongnam.module.heThong.dto.request.LoginRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.LoginResponse;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTroQuyen;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TaiKhoanRepository taiKhoanRepository;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        if (request == null) {
            throw new RuntimeException("Dữ liệu đăng nhập không hợp lệ");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email không được để trống");
        }

        if (request.getMatKhau() == null || request.getMatKhau().isBlank()) {
            throw new RuntimeException("Mật khẩu không được để trống");
        }

        String email = request.getEmail().trim().toLowerCase();

        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng"));

        if (taiKhoan.getTrangThai() == null) {
            throw new DisabledException("Tài khoản chưa có trạng thái");
        }

        if (taiKhoan.getTrangThai() == TrangThaiTaiKhoan.cho_kich_hoat) {
            throw new DisabledException("Tài khoản chưa được kích hoạt");
        }

        if (taiKhoan.getTrangThai() == TrangThaiTaiKhoan.bi_khoa) {
            throw new LockedException("Tài khoản đã bị khóa");
        }

        if (taiKhoan.getMatKhauHash() == null || taiKhoan.getMatKhauHash().isBlank()) {
            throw new RuntimeException("Tài khoản này chỉ đăng nhập bằng Google");
        }

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            request.getMatKhau()
                    )
            );
        } catch (AuthenticationException ex) {
            throw new RuntimeException("Email hoặc mật khẩu không đúng");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Set<TaiKhoanVaiTro> taiKhoanVaiTros = taiKhoan.getTaiKhoanVaiTros() == null
                ? Collections.emptySet()
                : taiKhoan.getTaiKhoanVaiTros();

        List<String> roles = taiKhoanVaiTros.stream()
                .filter(tv -> tv.getVaiTro() != null)
                .map(tv -> tv.getVaiTro().getMaVaiTro())
                .filter(value -> value != null && !value.isBlank())
                .distinct()
                .toList();

        List<String> permissions = taiKhoanVaiTros.stream()
                .filter(tv -> tv.getVaiTro() != null)
                .flatMap(tv -> {
                    Set<VaiTroQuyen> vaiTroQuyens = tv.getVaiTro().getVaiTroQuyens() == null
                            ? Collections.emptySet()
                            : tv.getVaiTro().getVaiTroQuyens();
                    return vaiTroQuyens.stream();
                })
                .filter(vtq -> vtq.getQuyen() != null)
                .map(vtq -> vtq.getQuyen().getMaQuyen())
                .filter(value -> value != null && !value.isBlank())
                .distinct()
                .toList();

        if (roles.isEmpty()) {
            throw new RuntimeException("Tài khoản chưa được gán vai trò");
        }

        taiKhoan.setLanDangNhapCuoi(LocalDateTime.now());
        taiKhoanRepository.save(taiKhoan);

        String token = jwtService.generateToken(userDetails);

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .taiKhoanId(taiKhoan.getId())
                .email(taiKhoan.getEmail())
                .loaiTaiKhoan(taiKhoan.getLoaiTaiKhoan() == null ? null : taiKhoan.getLoaiTaiKhoan().name())
                .trangThai(taiKhoan.getTrangThai() == null ? null : taiKhoan.getTrangThai().name())
                .roles(roles)
                .permissions(permissions)
                .build();
    }
}