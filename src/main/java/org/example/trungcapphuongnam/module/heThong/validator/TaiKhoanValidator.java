package org.example.trungcapphuongnam.module.heThong.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.HeThongException;
import org.example.trungcapphuongnam.module.heThong.dto.request.DoiMatKhauSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.DoiMatKhauTaiKhoanRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.GuiOtpDoiMatKhauSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.TaiKhoanRequest;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiTaiKhoan;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiTaiKhoan;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.repository.VaiTroRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaiKhoanValidator {

    private final TaiKhoanRepository taiKhoanRepository;
    private final VaiTroRepository vaiTroRepository;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public void validateTimTaiKhoanTheoEmail(String email) {
        if (isBlank(email)) {
            throw new HeThongException("Gmail tài khoản không được để trống");
        }

        if (!EMAIL_PATTERN.matcher(normalizeEmail(email)).matches()) {
            throw new HeThongException("Gmail tài khoản không đúng định dạng");
        }
    }

    public void validateCreate(TaiKhoanRequest request) {
        validateCommon(request);

        if (isBlank(request.getMatKhau())) {
            throw new HeThongException("Mật khẩu không được để trống");
        }

        validateMatKhau(request.getMatKhau());

        String email = normalizeEmail(request.getEmail());
        if (taiKhoanRepository.existsByEmailIgnoreCase(email)) {
            throw new HeThongException("Gmail này đã được sử dụng bởi tài khoản khác");
        }
    }

    public void validateUpdate(Long id, TaiKhoanRequest request) {
        if (id == null) {
            throw new HeThongException("Tài khoản cần cập nhật không hợp lệ");
        }

        if (!taiKhoanRepository.existsById(id)) {
            throw new HeThongException("Không tìm thấy tài khoản cần cập nhật");
        }

        validateCommon(request);

        if (!isBlank(request.getMatKhau())) {
            throw new HeThongException("Không được cập nhật mật khẩu tại trang cập nhật tài khoản. Vui lòng dùng trang đổi mật khẩu riêng");
        }

        String email = normalizeEmail(request.getEmail());
        if (taiKhoanRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new HeThongException("Gmail này đã được sử dụng bởi tài khoản khác");
        }
    }

    public void validateDoiMatKhauTaiKhoan(DoiMatKhauTaiKhoanRequest request) {
        if (request == null) {
            throw new HeThongException("Thông tin đổi mật khẩu không hợp lệ");
        }

        if (isBlank(request.getMatKhauMoi())) {
            throw new HeThongException("Mật khẩu mới không được để trống");
        }

        validateMatKhau(request.getMatKhauMoi());
    }

    public void validateDoiMatKhauTaiKhoanTheoGmail(String email, DoiMatKhauTaiKhoanRequest request) {
        validateTimTaiKhoanTheoEmail(email);
        validateDoiMatKhauTaiKhoan(request);
    }

    public void validateGuiOtpDoiMatKhauSinhVien(GuiOtpDoiMatKhauSinhVienRequest request) {
        if (request == null) {
            throw new HeThongException("Thông tin gửi OTP không hợp lệ");
        }

        if (isBlank(request.getEmail())) {
            throw new HeThongException("Gmail sinh viên không được để trống");
        }

        if (!EMAIL_PATTERN.matcher(normalizeEmail(request.getEmail())).matches()) {
            throw new HeThongException("Gmail sinh viên không đúng định dạng");
        }
    }

    public void validateDoiMatKhauSinhVien(DoiMatKhauSinhVienRequest request) {
        if (request == null) {
            throw new HeThongException("Thông tin đổi mật khẩu không hợp lệ");
        }

        if (isBlank(request.getEmail())) {
            throw new HeThongException("Gmail sinh viên không được để trống");
        }

        if (!EMAIL_PATTERN.matcher(normalizeEmail(request.getEmail())).matches()) {
            throw new HeThongException("Gmail sinh viên không đúng định dạng");
        }

        if (isBlank(request.getOtp())) {
            throw new HeThongException("Mã OTP không được để trống");
        }

        if (!request.getOtp().trim().matches("^\\d{6}$")) {
            throw new HeThongException("Mã OTP phải gồm 6 chữ số");
        }

        if (isBlank(request.getMatKhauMoi())) {
            throw new HeThongException("Mật khẩu mới không được để trống");
        }

        validateMatKhau(request.getMatKhauMoi());

        if (isBlank(request.getXacNhanMatKhau())) {
            throw new HeThongException("Xác nhận mật khẩu không được để trống");
        }

        if (!request.getMatKhauMoi().equals(request.getXacNhanMatKhau())) {
            throw new HeThongException("Xác nhận mật khẩu không khớp");
        }
    }

    private void validateCommon(TaiKhoanRequest request) {
        if (request == null) {
            throw new HeThongException("Dữ liệu tài khoản không hợp lệ");
        }

        if (isBlank(request.getEmail())) {
            throw new HeThongException("Gmail không được để trống");
        }

        String email = normalizeEmail(request.getEmail());
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new HeThongException("Gmail không đúng định dạng");
        }

        LoaiTaiKhoan loaiTaiKhoan = parseLoaiTaiKhoan(request.getLoaiTaiKhoan());
        parseTrangThaiTaiKhoan(request.getTrangThai());

        Set<String> roleCodes = chuanHoaRoleCodes(request.getRoles());
        if (roleCodes.isEmpty()) {
            throw new HeThongException("Tài khoản phải có ít nhất một vai trò");
        }

        for (String roleCode : roleCodes) {
            if (vaiTroRepository.findByMaVaiTro(roleCode).isEmpty()) {
                throw new HeThongException("Vai trò không tồn tại: " + roleCode);
            }
        }

        validateVaiTroTheoLoaiTaiKhoan(loaiTaiKhoan, roleCodes);
    }

    private void validateVaiTroTheoLoaiTaiKhoan(LoaiTaiKhoan loaiTaiKhoan, Set<String> roleCodes) {
        if (loaiTaiKhoan == null) {
            throw new HeThongException("Loại tài khoản không hợp lệ");
        }

        String roleBatBuoc = switch (loaiTaiKhoan) {
            case admin -> "ADMIN";
            case dao_tao -> "DAO_TAO";
            case nhan_vien -> "NHAN_VIEN";
            case giao_vien -> "GIAO_VIEN";
            case sinh_vien -> "SINH_VIEN";
        };

        if (!roleCodes.contains(roleBatBuoc)) {
            throw new HeThongException("Tài khoản loại " + loaiTaiKhoan.name() + " phải có vai trò " + roleBatBuoc);
        }
    }

    private LoaiTaiKhoan parseLoaiTaiKhoan(String value) {
        if (isBlank(value)) {
            throw new HeThongException("Loại tài khoản không được để trống");
        }

        try {
            return LoaiTaiKhoan.valueOf(value.trim().toLowerCase());
        } catch (Exception ex) {
            throw new HeThongException("Loại tài khoản không hợp lệ: " + value);
        }
    }

    private TrangThaiTaiKhoan parseTrangThaiTaiKhoan(String value) {
        if (isBlank(value)) {
            throw new HeThongException("Trạng thái tài khoản không được để trống");
        }

        try {
            return TrangThaiTaiKhoan.valueOf(value.trim().toLowerCase());
        } catch (Exception ex) {
            throw new HeThongException("Trạng thái tài khoản không hợp lệ: " + value);
        }
    }

    private Set<String> chuanHoaRoleCodes(List<String> roles) {
        if (roles == null) {
            return new LinkedHashSet<>();
        }

        return roles.stream()
                .filter(role -> !isBlank(role))
                .map(role -> role.trim().toUpperCase())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private void validateMatKhau(String matKhau) {
        if (matKhau.length() < 6) {
            throw new HeThongException("Mật khẩu phải có ít nhất 6 ký tự");
        }

        if (matKhau.length() > 100) {
            throw new HeThongException("Mật khẩu không được vượt quá 100 ký tự");
        }
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}