package org.example.trungcapphuongnam.common.security;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.RoleConstant;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final SinhVienRepository sinhVienRepository;
    private final GiaoVienRepository giaoVienRepository;

    @Transactional(readOnly = true)
    public TaiKhoan getTaiKhoan() {
        String email = getEmailDangNhap();
        return taiKhoanRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản đang đăng nhập: " + email));
    }

    @Transactional(readOnly = true)
    public Long getTaiKhoanId() {
        return getTaiKhoan().getId();
    }

    @Transactional(readOnly = true)
    public Long getSinhVienId() {
        Long taiKhoanId = getTaiKhoanId();
        return sinhVienRepository.findByTaiKhoanId(taiKhoanId)
                .orElseThrow(() -> new AccessDeniedException("Tài khoản hiện tại không phải sinh viên"))
                .getId();
    }

    @Transactional(readOnly = true)
    public Long getGiaoVienId() {
        Long taiKhoanId = getTaiKhoanId();
        return giaoVienRepository.findByTaiKhoanId(taiKhoanId)
                .orElseThrow(() -> new AccessDeniedException("Tài khoản hiện tại không phải giảng viên"))
                .getId();
    }

    public boolean isAdmin() {
        return hasRole(RoleConstant.ADMIN);
    }

    public boolean isDaoTao() {
        return hasRole(RoleConstant.DAO_TAO);
    }

    public boolean isGiaoVien() {
        return hasRole(RoleConstant.GIAO_VIEN);
    }

    public boolean isSinhVien() {
        return hasRole(RoleConstant.SINH_VIEN);
    }

    public boolean hasRole(String role) {
        String roleName = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.getAuthorities() != null
                && authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(roleName::equalsIgnoreCase);
    }

    private String getEmailDangNhap() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new AccessDeniedException("Chưa đăng nhập");
        }
        return authentication.getName().trim().toLowerCase();
    }
}
