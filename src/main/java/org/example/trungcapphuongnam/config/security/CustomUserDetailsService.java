package org.example.trungcapphuongnam.config.security;

import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTroQuyen;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final TaiKhoanRepository taiKhoanRepository;

    public CustomUserDetailsService(TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if (email == null || email.isBlank()) {
            throw new UsernameNotFoundException("Email không hợp lệ");
        }

        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + email));

        Set<SimpleGrantedAuthority> authorities = new LinkedHashSet<>();

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
            throw new UsernameNotFoundException("Tài khoản chưa được gán vai trò: " + email);
        }

        return User.builder()
                .username(taiKhoan.getEmail())
                .password(taiKhoan.getMatKhauHash() == null ? "" : taiKhoan.getMatKhauHash())
                .authorities(authorities)
                .accountLocked(isLocked(taiKhoan))
                .disabled(!isEnabled(taiKhoan))
                .accountExpired(false)
                .credentialsExpired(false)
                .build();
    }

    private String formatRole(String role) {
        String value = role.trim().toUpperCase();

        if (value.startsWith("ROLE_")) {
            return value;
        }

        return "ROLE_" + value;
    }

    private boolean isEnabled(TaiKhoan taiKhoan) {
        if (taiKhoan.getTrangThai() == null) {
            return false;
        }

        String trangThai = taiKhoan.getTrangThai().toString();

        return trangThai.equalsIgnoreCase("DA_KICH_HOAT")
                || trangThai.equalsIgnoreCase("DAKICHHOAT")
                || trangThai.equalsIgnoreCase("ACTIVE")
                || trangThai.equalsIgnoreCase("HOAT_DONG");
    }

    private boolean isLocked(TaiKhoan taiKhoan) {
        if (taiKhoan.getTrangThai() == null) {
            return true;
        }

        String trangThai = taiKhoan.getTrangThai().toString();

        return trangThai.equalsIgnoreCase("BI_KHOA")
                || trangThai.equalsIgnoreCase("LOCKED")
                || trangThai.equalsIgnoreCase("KHOA");
    }
}