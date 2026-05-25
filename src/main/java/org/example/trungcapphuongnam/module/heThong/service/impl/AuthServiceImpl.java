package org.example.trungcapphuongnam.module.heThong.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.config.jwt.JwtService;
import org.example.trungcapphuongnam.config.security.CustomUserDetailsService;
import org.example.trungcapphuongnam.module.heThong.dto.request.LoginRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.LoginResponse;
import org.example.trungcapphuongnam.module.heThong.entity.NhatKyDangNhap;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTroQuyen;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiTaiKhoan;
import org.example.trungcapphuongnam.module.heThong.repository.NhatKyDangNhapRepository;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.service.AuthService;
import org.example.trungcapphuongnam.module.heThong.service.ThongTinTaiKhoanLogResolver;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final NhatKyDangNhapRepository nhatKyDangNhapRepository;
    private final ThongTinTaiKhoanLogResolver thongTinTaiKhoanLogResolver;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        TaiKhoan taiKhoan = null;
        String emailNhap = request == null ? null : request.getEmail();

        try {
            if (request == null) {
                throw new BadRequestException("Dữ liệu đăng nhập không hợp lệ");
            }

            if (request.getEmail() == null || request.getEmail().isBlank()) {
                throw new BadRequestException("Email không được để trống");
            }

            if (request.getMatKhau() == null || request.getMatKhau().isBlank()) {
                throw new BadRequestException("Mật khẩu không được để trống");
            }

            String email = request.getEmail().trim().toLowerCase();

            taiKhoan = taiKhoanRepository.findByEmail(email)
                    .orElseThrow(() -> new BadRequestException("Email hoặc mật khẩu không đúng"));

            if (taiKhoan.getTrangThai() == null) {
                throw new BadRequestException("Tài khoản chưa có trạng thái");
            }

            if (taiKhoan.getTrangThai() == TrangThaiTaiKhoan.cho_kich_hoat) {
                throw new BadRequestException("Tài khoản chưa được kích hoạt");
            }

            if (taiKhoan.getTrangThai() == TrangThaiTaiKhoan.bi_khoa) {
                throw new BadRequestException("Tài khoản đã bị khóa");
            }

            if (taiKhoan.getMatKhauHash() == null || taiKhoan.getMatKhauHash().isBlank()) {
                throw new BadRequestException("Tài khoản này chỉ đăng nhập bằng Google");
            }

            if (!passwordEncoder.matches(request.getMatKhau(), taiKhoan.getMatKhauHash())) {
                throw new BadRequestException("Email hoặc mật khẩu không đúng");
            }

            UserDetails userDetails;

            try {
                userDetails = customUserDetailsService.loadUserByUsername(email);
            } catch (UsernameNotFoundException ex) {
                throw new BadRequestException(ex.getMessage());
            }

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
                throw new BadRequestException("Tài khoản chưa được gán vai trò");
            }

            taiKhoan.setLanDangNhapCuoi(LocalDateTime.now());
            taiKhoanRepository.save(taiKhoan);

            ghiNhatKyDangNhap(emailNhap, taiKhoan, true, null);

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
        } catch (BadRequestException ex) {
            ghiNhatKyDangNhap(emailNhap, taiKhoan, false, ex.getMessage());
            throw ex;
        }
    }

    private void ghiNhatKyDangNhap(String emailNhap, TaiKhoan taiKhoan, Boolean thanhCong, String lyDoThatBai) {
        ThongTinTaiKhoanLogResolver.ThongTinTaiKhoanLog thongTin =
                thongTinTaiKhoanLogResolver.resolve(taiKhoan);

        NhatKyDangNhap entity = NhatKyDangNhap.builder()
                .taiKhoan(taiKhoan)
                .emailNhap(emailNhap)
                .taiKhoanEmail(taiKhoan == null ? null : taiKhoan.getEmail())
                .tenTaiKhoan(thongTin.getTenTaiKhoan())
                .loaiTaiKhoan(taiKhoan == null || taiKhoan.getLoaiTaiKhoan() == null ? null : taiKhoan.getLoaiTaiKhoan().name())
                .doiTuongLoai(thongTin.getDoiTuongLoai())
                .doiTuongId(thongTin.getDoiTuongId())
                .doiTuongTen(thongTin.getDoiTuongTen())
                .phuongThuc("MAT_KHAU")
                .ipAddress(getIpAddress())
                .userAgent(getUserAgent())
                .thanhCong(thanhCong)
                .lyDoThatBai(lyDoThatBai)
                .build();

        nhatKyDangNhapRepository.save(entity);
    }

    private String getIpAddress() {
        HttpServletRequest request = getRequest();

        if (request == null) {
            return null;
        }

        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    private String getUserAgent() {
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getHeader("User-Agent");
    }

    private HttpServletRequest getRequest() {
        if (!(RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes)) {
            return null;
        }

        return attributes.getRequest();
    }
}