package org.example.trungcapphuongnam.module.sinhVien.validator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.sinhVien.SinhVienException;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.TiepNhanSinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SinhVienValidator {

    private static final long MAX_ANH_SIZE = 10L * 1024L * 1024L;
    private static final long MAX_TAI_LIEU_SIZE = 25L * 1024L * 1024L;

    private final SinhVienRepository sinhVienRepository;
    private final NganhRepository nganhRepository;
    private final ChuongTrinhRepository chuongTrinhRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final TaiKhoanRepository taiKhoanRepository;

    public ThongTinChuongTrinh validateTiepNhan(TiepNhanSinhVienRequest request) {
        if (request == null) {
            throw new SinhVienException("Thông tin tiếp nhận sinh viên không được để trống");
        }

        requireId(request.getNganhId(), "Ngành");
        requireId(request.getChuongTrinhId(), "Chương trình");
        requireId(request.getChuongTrinhVersionId(), "Version chương trình");
        requireText(request.getHoTen(), "Họ tên sinh viên");
        requireText(request.getEmail(), "Gmail sinh viên");
        requireText(request.getSoDienThoai(), "Số điện thoại sinh viên");
        requireText(request.getDiaChi(), "Địa chỉ liên hệ");
        requireText(request.getDiaChiThuongTru(), "Địa chỉ thường trú");
        requireText(request.getSoCccd(), "Số CCCD");
        requireText(request.getNoiCapCccd(), "Nơi cấp CCCD");
        requireText(request.getBangCap(), "Bằng cấp");
        requireText(request.getTruongTotNghiep(), "Trường tốt nghiệp");
        requireText(request.getHoTenCha(), "Họ tên cha");
        requireText(request.getSdtCha(), "Số điện thoại cha");
        requireText(request.getHoTenMe(), "Họ tên mẹ");
        requireText(request.getSdtMe(), "Số điện thoại mẹ");
        requireText(request.getHoTenNguoiThan(), "Họ tên người thân");
        requireText(request.getQuanHeNguoiThan(), "Quan hệ người thân");
        requireText(request.getSdtNguoiThan(), "Số điện thoại người thân");
        requireText(request.getEmailNguoiThan(), "Gmail người thân");

        if (request.getNgaySinh() == null) {
            throw new SinhVienException("Ngày sinh không được để trống");
        }
        if (!request.getNgaySinh().isBefore(LocalDate.now())) {
            throw new SinhVienException("Ngày sinh phải nhỏ hơn ngày hiện tại");
        }
        if (request.getGioiTinh() == null) {
            throw new SinhVienException("Giới tính không được để trống");
        }
        if (request.getNgayCapCccd() == null) {
            throw new SinhVienException("Ngày cấp CCCD không được để trống");
        }
        if (request.getNgayCapCccd().isAfter(LocalDate.now())) {
            throw new SinhVienException("Ngày cấp CCCD không được lớn hơn ngày hiện tại");
        }
        if (request.getNamTotNghiep() == null) {
            throw new SinhVienException("Năm tốt nghiệp không được để trống");
        }
        int namHienTai = LocalDate.now().getYear();
        if (request.getNamTotNghiep() < 1950 || request.getNamTotNghiep() > namHienTai) {
            throw new SinhVienException("Năm tốt nghiệp không hợp lệ");
        }

        String email = normalizeEmail(request.getEmail());
        request.setEmail(email);
        if (sinhVienRepository.existsByEmail(email) || taiKhoanRepository.existsByEmail(email)) {
            throw new SinhVienException("Gmail sinh viên đã tồn tại");
        }

        if (sinhVienRepository.existsBySoCccd(request.getSoCccd().trim())) {
            throw new SinhVienException("Số CCCD đã tồn tại");
        }

        nganhRepository.findById(request.getNganhId())
                .orElseThrow(() -> new SinhVienException("Không tìm thấy ngành"));

        ChuongTrinh chuongTrinh = chuongTrinhRepository.findById(request.getChuongTrinhId())
                .orElseThrow(() -> new SinhVienException("Không tìm thấy chương trình"));

        if (!Objects.equals(chuongTrinh.getNganhId(), request.getNganhId())) {
            throw new SinhVienException("Chương trình không thuộc ngành đã chọn");
        }

        ChuongTrinhVersion version = chuongTrinhVersionRepository.findById(request.getChuongTrinhVersionId())
                .orElseThrow(() -> new SinhVienException("Không tìm thấy version chương trình"));

        if (!Objects.equals(version.getChuongTrinhId(), request.getChuongTrinhId())) {
            throw new SinhVienException("Version chương trình không thuộc chương trình đã chọn");
        }

        if (!Boolean.TRUE.equals(version.getLaHienHanh())) {
            throw new SinhVienException("Chỉ được tiếp nhận sinh viên vào version chương trình đang hiện hành");
        }

        return new ThongTinChuongTrinh(chuongTrinh, version);
    }

    public void validateTepTiepNhan(
            MultipartFile anhChanDung,
            MultipartFile cccdTruoc,
            MultipartFile cccdSau,
            MultipartFile bangCap
    ) {
        if (anhChanDung != null && !anhChanDung.isEmpty()) {
            validateImage(anhChanDung, "Ảnh chân dung");
        }

        if (cccdTruoc != null && !cccdTruoc.isEmpty()) {
            validateImageOrPdf(cccdTruoc, "CCCD mặt trước");
        }

        if (cccdSau != null && !cccdSau.isEmpty()) {
            validateImageOrPdf(cccdSau, "CCCD mặt sau");
        }

        if (bangCap != null && !bangCap.isEmpty()) {
            validateImageOrPdf(bangCap, "Bằng cấp");
        }
    }

    public void validateTepKhac(MultipartFile file, String ten) {
        if (file == null || file.isEmpty()) {
            return;
        }
        if (file.getSize() > MAX_TAI_LIEU_SIZE) {
            throw new SinhVienException(ten + " vượt quá dung lượng cho phép 25MB");
        }
    }

    public void validateCreateThuCong(SinhVienRequest request) {
        if (request == null) {
            throw new SinhVienException("Thông tin sinh viên không được để trống");
        }
        requireText(request.getMaSinhVien(), "Mã sinh viên");
        requireText(request.getHoTen(), "Họ tên sinh viên");
        requireText(request.getEmail(), "Email sinh viên");

        String email = normalizeEmail(request.getEmail());
        request.setEmail(email);

        if (sinhVienRepository.existsByMaSinhVien(request.getMaSinhVien())) {
            throw new SinhVienException("Mã sinh viên đã tồn tại");
        }
        if (sinhVienRepository.existsByEmail(email)) {
            throw new SinhVienException("Email sinh viên đã tồn tại");
        }
        if (request.getTaiKhoanId() != null && sinhVienRepository.existsByTaiKhoanId(request.getTaiKhoanId())) {
            throw new SinhVienException("Tài khoản đã được gắn với sinh viên khác");
        }
    }

    public void validateUpdate(Long id, SinhVienRequest request) {
        if (id == null) {
            throw new SinhVienException("ID sinh viên không được để trống");
        }
        if (request == null) {
            throw new SinhVienException("Thông tin sinh viên không được để trống");
        }
        requireText(request.getMaSinhVien(), "Mã sinh viên");
        requireText(request.getHoTen(), "Họ tên sinh viên");
        requireText(request.getEmail(), "Email sinh viên");

        String email = normalizeEmail(request.getEmail());
        request.setEmail(email);

        if (sinhVienRepository.existsByMaSinhVienAndIdNot(request.getMaSinhVien(), id)) {
            throw new SinhVienException("Mã sinh viên đã tồn tại");
        }

        if (sinhVienRepository.existsByEmailIgnoreCaseAndIdNot(email, id)) {
            throw new SinhVienException("Email sinh viên đã tồn tại");
        }

        if (request.getTaiKhoanId() != null
                && sinhVienRepository.existsByTaiKhoanIdAndIdNot(request.getTaiKhoanId(), id)) {
            throw new SinhVienException("Tài khoản đã được gắn với sinh viên khác");
        }
    }

    public String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    private void validateImage(MultipartFile file, String ten) {
        validateDungLuong(file, ten, MAX_ANH_SIZE, "10MB");
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        if (!contentType.startsWith("image/")) {
            throw new SinhVienException(ten + " phải là file hình ảnh");
        }
    }

    private void validateImageOrPdf(MultipartFile file, String ten) {
        validateDungLuong(file, ten, MAX_TAI_LIEU_SIZE, "25MB");
        String contentType = file.getContentType() == null ? "" : file.getContentType().toLowerCase(Locale.ROOT);
        String filename = file.getOriginalFilename() == null ? "" : file.getOriginalFilename().toLowerCase(Locale.ROOT);
        boolean hopLe = contentType.startsWith("image/") || contentType.equals("application/pdf") || filename.endsWith(".pdf");
        if (!hopLe) {
            throw new SinhVienException(ten + " phải là hình ảnh hoặc PDF");
        }
    }

    private void validateDungLuong(MultipartFile file, String ten, long maxSize, String maxText) {
        if (file.getSize() > maxSize) {
            throw new SinhVienException(ten + " vượt quá dung lượng cho phép " + maxText);
        }
    }

    private void requireFile(MultipartFile file, String ten) {
        if (file == null || file.isEmpty()) {
            throw new SinhVienException(ten + " không được để trống");
        }
    }

    private void requireId(Long id, String ten) {
        if (id == null) {
            throw new SinhVienException(ten + " không được để trống");
        }
    }

    private void requireText(String value, String ten) {
        if (value == null || value.trim().isEmpty()) {
            throw new SinhVienException(ten + " không được để trống");
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ThongTinChuongTrinh {
        private final ChuongTrinh chuongTrinh;
        private final ChuongTrinhVersion version;
    }
}
