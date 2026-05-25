package org.example.trungcapphuongnam.module.heThong.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.entity.NhatKyHeThong;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.repository.NhatKyHeThongRepository;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.service.NhatKyHeThongWriter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class NhatKyHeThongWriterImpl implements NhatKyHeThongWriter {

    private final NhatKyHeThongRepository repository;
    private final TaiKhoanRepository taiKhoanRepository;

    @Override
    public void ghi(
            String hanhDong,
            String bangTacDong,
            Long banGhiId,
            String noiDungCu,
            String noiDungMoi
    ) {
        TaiKhoan taiKhoan = getTaiKhoanDangNhap();
        HttpServletRequest request = getRequest();

        String moduleNghiepVu = xacDinhModuleTuBangTacDong(bangTacDong);
        String chucNang = bangTacDong;
        String moTa = taoMoTaMacDinh(hanhDong, bangTacDong, banGhiId);

        NhatKyHeThong entity = NhatKyHeThong.builder()
                .taiKhoan(taiKhoan)
                .taiKhoanEmail(taiKhoan == null ? getEmailDangNhap() : taiKhoan.getEmail())
                .tenTaiKhoan(taiKhoan == null ? getEmailDangNhap() : taiKhoan.getEmail())
                .loaiTaiKhoan(taiKhoan == null || taiKhoan.getLoaiTaiKhoan() == null ? null : taiKhoan.getLoaiTaiKhoan().name())
                .doiTuongLoai(taiKhoan == null || taiKhoan.getLoaiTaiKhoan() == null ? null : taiKhoan.getLoaiTaiKhoan().name())
                .doiTuongId(taiKhoan == null ? null : taiKhoan.getId())
                .doiTuongMa(taiKhoan == null ? null : String.valueOf(taiKhoan.getId()))
                .doiTuongTen(taiKhoan == null ? getEmailDangNhap() : taiKhoan.getEmail())
                .doiTuongEmail(taiKhoan == null ? getEmailDangNhap() : taiKhoan.getEmail())
                .moduleNghiepVu(moduleNghiepVu)
                .chucNang(chucNang)
                .hanhDong(chuanHoa(hanhDong))
                .bangTacDong(chuanHoa(bangTacDong))
                .banGhiId(banGhiId)
                .banGhiMa(banGhiId == null ? null : String.valueOf(banGhiId))
                .banGhiTen(null)
                .noiDungCu(noiDungCu)
                .noiDungMoi(noiDungMoi)
                .moTa(moTa)
                .phuongThuc(request == null ? null : request.getMethod())
                .duongDan(request == null ? null : layDuongDanDayDu(request))
                .ketQua("THANH_CONG")
                .ipAddress(getIpAddress())
                .userAgent(getUserAgent())
                .duLieuTimKiem(taoDuLieuTimKiem(
                        moduleNghiepVu,
                        chucNang,
                        hanhDong,
                        bangTacDong,
                        banGhiId,
                        null,
                        null,
                        taiKhoan,
                        moTa,
                        noiDungCu,
                        noiDungMoi
                ))
                .build();

        repository.save(entity);
    }

    @Override
    public void ghiChiTiet(
            String moduleNghiepVu,
            String chucNang,
            String hanhDong,
            String bangTacDong,
            Long banGhiId,
            String banGhiMa,
            String banGhiTen,
            String noiDungCu,
            String noiDungMoi,
            String moTa
    ) {
        TaiKhoan taiKhoan = getTaiKhoanDangNhap();
        HttpServletRequest request = getRequest();

        String module = coGiaTri(moduleNghiepVu) ? moduleNghiepVu : xacDinhModuleTuBangTacDong(bangTacDong);
        String chucNangLog = coGiaTri(chucNang) ? chucNang : bangTacDong;

        NhatKyHeThong entity = NhatKyHeThong.builder()
                .taiKhoan(taiKhoan)
                .taiKhoanEmail(taiKhoan == null ? getEmailDangNhap() : taiKhoan.getEmail())
                .tenTaiKhoan(taiKhoan == null ? getEmailDangNhap() : taiKhoan.getEmail())
                .loaiTaiKhoan(taiKhoan == null || taiKhoan.getLoaiTaiKhoan() == null ? null : taiKhoan.getLoaiTaiKhoan().name())
                .doiTuongLoai(taiKhoan == null || taiKhoan.getLoaiTaiKhoan() == null ? null : taiKhoan.getLoaiTaiKhoan().name())
                .doiTuongId(taiKhoan == null ? null : taiKhoan.getId())
                .doiTuongMa(taiKhoan == null ? null : String.valueOf(taiKhoan.getId()))
                .doiTuongTen(taiKhoan == null ? getEmailDangNhap() : taiKhoan.getEmail())
                .doiTuongEmail(taiKhoan == null ? getEmailDangNhap() : taiKhoan.getEmail())
                .moduleNghiepVu(chuanHoa(module))
                .chucNang(chuanHoa(chucNangLog))
                .hanhDong(chuanHoa(hanhDong))
                .bangTacDong(chuanHoa(bangTacDong))
                .banGhiId(banGhiId)
                .banGhiMa(banGhiMa)
                .banGhiTen(banGhiTen)
                .noiDungCu(noiDungCu)
                .noiDungMoi(noiDungMoi)
                .moTa(moTa)
                .phuongThuc(request == null ? null : request.getMethod())
                .duongDan(request == null ? null : layDuongDanDayDu(request))
                .ketQua("THANH_CONG")
                .ipAddress(getIpAddress())
                .userAgent(getUserAgent())
                .duLieuTimKiem(taoDuLieuTimKiem(
                        module,
                        chucNangLog,
                        hanhDong,
                        bangTacDong,
                        banGhiId,
                        banGhiMa,
                        banGhiTen,
                        taiKhoan,
                        moTa,
                        noiDungCu,
                        noiDungMoi
                ))
                .build();

        repository.save(entity);
    }

    private TaiKhoan getTaiKhoanDangNhap() {
        String email = getEmailDangNhap();

        if (!coGiaTri(email)) {
            return null;
        }

        return taiKhoanRepository.findByEmail(email).orElse(null);
    }

    private String getEmailDangNhap() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String email = authentication.getName();

        if (!coGiaTri(email) || "anonymousUser".equals(email)) {
            return null;
        }

        return email;
    }

    private String getIpAddress() {
        HttpServletRequest request = getRequest();

        if (request == null) {
            return null;
        }

        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (coGiaTri(forwardedFor)) {
            return forwardedFor.split(",")[0].trim();
        }

        String realIp = request.getHeader("X-Real-IP");

        if (coGiaTri(realIp)) {
            return realIp.trim();
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

    private String layDuongDanDayDu(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String query = request.getQueryString();

        if (!coGiaTri(query)) {
            return uri;
        }

        return uri + "?" + query;
    }

    private String xacDinhModuleTuBangTacDong(String bangTacDong) {
        String value = chuanHoa(bangTacDong);

        if (!coGiaTri(value)) {
            return "KHAC";
        }

        if (value.contains("SINH_VIEN")) {
            return "SINH_VIEN";
        }

        if (value.contains("GIANG_DAY")
                || value.contains("LOP_HOC_PHAN")
                || value.contains("PHAN_CONG")
                || value.contains("DIEM_DANH")) {
            return "GIANG_DAY";
        }

        if (value.contains("CHUONG_TRINH")
                || value.contains("NGANH")
                || value.contains("MON_HOC")
                || value.contains("SYLLABUS")) {
            return "CHUONG_TRINH";
        }

        if (value.contains("DIEM")) {
            return "DIEM";
        }

        if (value.contains("HOC_PHI")) {
            return "HOC_PHI";
        }

        if (value.contains("THI")) {
            return "THI";
        }

        if (value.contains("LMS")) {
            return "LMS";
        }

        if (value.contains("TAI_KHOAN")
                || value.contains("VAI_TRO")
                || value.contains("QUYEN")
                || value.contains("PHAN_QUYEN")
                || value.contains("MAIL")
                || value.contains("OTP")
                || value.contains("TEP_DINH_KEM")
                || value.contains("NHAT_KY")) {
            return "HE_THONG";
        }

        return "KHAC";
    }

    private String taoMoTaMacDinh(String hanhDong, String bangTacDong, Long banGhiId) {
        StringBuilder builder = new StringBuilder();

        if (coGiaTri(hanhDong)) {
            builder.append(chuanHoa(hanhDong));
        }

        if (coGiaTri(bangTacDong)) {
            if (!builder.isEmpty()) {
                builder.append(" ");
            }
            builder.append(chuanHoa(bangTacDong));
        }

        if (banGhiId != null) {
            builder.append(" ID ").append(banGhiId);
        }

        return builder.toString();
    }

    private String taoDuLieuTimKiem(
            String moduleNghiepVu,
            String chucNang,
            String hanhDong,
            String bangTacDong,
            Long banGhiId,
            String banGhiMa,
            String banGhiTen,
            TaiKhoan taiKhoan,
            String moTa,
            String noiDungCu,
            String noiDungMoi
    ) {
        StringBuilder builder = new StringBuilder();

        them(builder, moduleNghiepVu);
        them(builder, chucNang);
        them(builder, hanhDong);
        them(builder, bangTacDong);
        them(builder, banGhiId == null ? null : String.valueOf(banGhiId));
        them(builder, banGhiMa);
        them(builder, banGhiTen);
        them(builder, moTa);
        them(builder, noiDungCu);
        them(builder, noiDungMoi);

        if (taiKhoan != null) {
            them(builder, taiKhoan.getId() == null ? null : String.valueOf(taiKhoan.getId()));
            them(builder, taiKhoan.getEmail());
            them(builder, taiKhoan.getLoaiTaiKhoan() == null ? null : taiKhoan.getLoaiTaiKhoan().name());
        } else {
            them(builder, getEmailDangNhap());
        }

        return builder.toString().toLowerCase();
    }

    private void them(StringBuilder builder, String value) {
        if (!coGiaTri(value)) {
            return;
        }

        if (!builder.isEmpty()) {
            builder.append(" | ");
        }

        builder.append(value.trim());
    }

    private String chuanHoa(String value) {
        if (!coGiaTri(value)) {
            return null;
        }

        return value.trim().toUpperCase();
    }

    private boolean coGiaTri(String value) {
        return value != null && !value.trim().isEmpty();
    }
}