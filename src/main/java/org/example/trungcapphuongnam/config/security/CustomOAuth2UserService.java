package org.example.trungcapphuongnam.config.security;

import org.example.trungcapphuongnam.common.enums.TrangThaiTaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTroQuyen;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final TaiKhoanRepository taiKhoanRepository;

    public CustomOAuth2UserService(TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        String email = (String) attributes.get("email");

        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("email_not_found"),
                    "Không lấy được email từ Google"
            );
        }

        String normalizedEmail = email.trim().toLowerCase();

        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new OAuth2AuthenticationException(
                        new OAuth2Error("account_not_found"),
                        "Tài khoản chưa được cấp quyền đăng nhập hệ thống"
                ));

        if (taiKhoan.getTrangThai() == null) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("account_status_invalid"),
                    "Tài khoản chưa có trạng thái"
            );
        }

        if (taiKhoan.getTrangThai() == TrangThaiTaiKhoan.cho_kich_hoat) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("account_not_activated"),
                    "Tài khoản chưa được kích hoạt"
            );
        }

        if (taiKhoan.getTrangThai() == TrangThaiTaiKhoan.bi_khoa ) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("account_locked"),
                    "Tài khoản đã bị khóa"
            );
        }

        Set<GrantedAuthority> authorities = new LinkedHashSet<>();

        if (taiKhoan.getTaiKhoanVaiTros() != null) {
            for (TaiKhoanVaiTro taiKhoanVaiTro : taiKhoan.getTaiKhoanVaiTros()) {
                if (taiKhoanVaiTro.getVaiTro() == null) {
                    continue;
                }

                String maVaiTro = taiKhoanVaiTro.getVaiTro().getMaVaiTro();

                if (maVaiTro != null && !maVaiTro.isBlank()) {
                    authorities.add(new SimpleGrantedAuthority(formatRole(maVaiTro)));
                }

                if (taiKhoanVaiTro.getVaiTro().getVaiTroQuyens() != null) {
                    for (VaiTroQuyen vaiTroQuyen : taiKhoanVaiTro.getVaiTro().getVaiTroQuyens()) {
                        if (vaiTroQuyen.getQuyen() == null) {
                            continue;
                        }

                        String maQuyen = vaiTroQuyen.getQuyen().getMaQuyen();

                        if (maQuyen != null && !maQuyen.isBlank()) {
                            authorities.add(new SimpleGrantedAuthority(maQuyen.trim().toUpperCase()));
                        }
                    }
                }
            }
        }

        if (authorities.isEmpty()) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("role_not_found"),
                    "Tài khoản chưa được gán vai trò"
            );
        }

        return new DefaultOAuth2User(authorities, attributes, "email");
    }

    private String formatRole(String role) {
        String value = role.trim().toUpperCase();

        if (value.startsWith("ROLE_")) {
            return value;
        }

        return "ROLE_" + value;
    }
}