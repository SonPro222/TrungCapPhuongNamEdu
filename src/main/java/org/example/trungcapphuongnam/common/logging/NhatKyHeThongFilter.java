package org.example.trungcapphuongnam.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.trungcapphuongnam.module.heThong.entity.NhatKyHeThong;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.repository.NhatKyHeThongRepository;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.service.ThongTinTaiKhoanLogResolver;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class NhatKyHeThongFilter extends OncePerRequestFilter {

    private final NhatKyHeThongRepository nhatKyHeThongRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final ThongTinTaiKhoanLogResolver thongTinTaiKhoanLogResolver;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (uri == null) {
            return true;
        }

        if (uri.startsWith("/api/auth/login")
                || uri.startsWith("/api/he-thong/nhat-ky-dang-nhap")
                || uri.startsWith("/api/he-thong/nhat-ky-he-thong")
                || uri.startsWith("/actuator")
                || uri.startsWith("/swagger")
                || uri.startsWith("/v3/api-docs")) {
            return true;
        }

        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // Bỏ audit các GET thông thường (xem, load, search, page, list)
        // Chỉ giữ lại GET cho dữ liệu nhạy cảm: bảng điểm, học phí/tài chính
        if ("GET".equalsIgnoreCase(method)) {
            return !laDuLieuNhayCamGet(uri);
        }

        return false;
    }

    /**
     * Các GET endpoint nhạy cảm cần audit:
     * - /api/diem/**     : bảng điểm sinh viên
     * - /api/hoc-phi/**  : thông tin tài chính, học phí
     */
    private boolean laDuLieuNhayCamGet(String uri) {
        return uri.contains("/diem")
                || uri.contains("/hoc-phi");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        Exception loi = null;

        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            loi = ex;
            throw ex;
        } finally {
            ghiNhatKy(request, response, loi);
        }
    }

    private void ghiNhatKy(HttpServletRequest request, HttpServletResponse response, Exception loi) {
        try {
            String method = request.getMethod();
            String uri = request.getRequestURI();

            TaiKhoan taiKhoan = getTaiKhoanDangNhap();
            ThongTinTaiKhoanLogResolver.ThongTinTaiKhoanLog thongTin =
                    thongTinTaiKhoanLogResolver.resolve(taiKhoan);

            String taiKhoanEmail = taiKhoan == null ? getEmailDangNhap() : taiKhoan.getEmail();
            String tenTaiKhoan = layGiaTri(thongTin.getTenTaiKhoan(), taiKhoanEmail);
            String module = xacDinhModule(uri);
            String chucNang = xacDinhChucNang(uri);
            String hanhDong = xacDinhHanhDong(method, uri);
            String ketQua = loi == null && response.getStatus() < 400 ? "THANH_CONG" : "THAT_BAI";
            String duongDan = layDuongDanDayDu(request);
            Long banGhiId = layBanGhiIdTuUri(uri);

            String moTa = taoMoTa(
                    tenTaiKhoan,
                    taiKhoanEmail,
                    hanhDong,
                    module,
                    chucNang,
                    duongDan,
                    ketQua
            );

            NhatKyHeThong entity = NhatKyHeThong.builder()
                    .taiKhoan(taiKhoan)
                    .taiKhoanEmail(taiKhoanEmail)
                    .tenTaiKhoan(tenTaiKhoan)
                    .loaiTaiKhoan(taiKhoan == null || taiKhoan.getLoaiTaiKhoan() == null ? null : taiKhoan.getLoaiTaiKhoan().name())
                    .doiTuongLoai(thongTin.getDoiTuongLoai())
                    .doiTuongId(thongTin.getDoiTuongId())
                    .doiTuongMa(thongTin.getDoiTuongId() == null ? null : String.valueOf(thongTin.getDoiTuongId()))
                    .doiTuongTen(layGiaTri(thongTin.getDoiTuongTen(), tenTaiKhoan))
                    .doiTuongEmail(taiKhoanEmail)
                    .hanhDong(hanhDong)
                    .moduleNghiepVu(module)
                    .chucNang(chucNang)
                    .moTa(moTa)
                    .bangTacDong(chucNang)
                    .banGhiId(banGhiId)
                    .banGhiMa(banGhiId == null ? null : String.valueOf(banGhiId))
                    .banGhiTen(null)
                    .phuongThuc(method)
                    .duongDan(duongDan)
                    .ketQua(ketQua)
                    .ipAddress(getIpAddress(request))
                    .userAgent(request.getHeader("User-Agent"))
                    .noiDungCu(null)
                    .noiDungMoi(null)
                    .duLieuTimKiem(taoDuLieuTimKiem(
                            taiKhoanEmail,
                            tenTaiKhoan,
                            thongTin.getDoiTuongLoai(),
                            thongTin.getDoiTuongId(),
                            thongTin.getDoiTuongTen(),
                            module,
                            chucNang,
                            hanhDong,
                            ketQua,
                            duongDan,
                            banGhiId,
                            moTa
                    ))
                    .build();

            nhatKyHeThongRepository.save(entity);
            log.info("Đã ghi nhật ký hệ thống: {} {} {}", method, uri, taiKhoanEmail);
        } catch (Exception ex) {
            log.error("Lỗi ghi nhật ký hệ thống: {}", ex.getMessage(), ex);
        }
    }

    private TaiKhoan getTaiKhoanDangNhap() {
        String email = getEmailDangNhap();

        if (email == null || email.isBlank()) {
            return null;
        }

        return taiKhoanRepository.findByEmailIgnoreCase(email).orElse(null);
    }

    private String getEmailDangNhap() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String email = authentication.getName();

        if (email == null || email.isBlank() || "anonymousUser".equals(email)) {
            return null;
        }

        return email;
    }

    private String getIpAddress(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");

        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }

        String realIp = request.getHeader("X-Real-IP");

        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }

        return request.getRemoteAddr();
    }

    private String layDuongDanDayDu(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String query = request.getQueryString();

        if (query == null || query.isBlank()) {
            return uri;
        }

        return uri + "?" + query;
    }

    private String xacDinhHanhDong(String method) {
        return xacDinhHanhDong(method, null);
    }

    private String xacDinhHanhDong(String method, String uri) {
        // Ưu tiên detect hành động đặc biệt từ URI trước
        if (uri != null) {
            if (uri.contains("/mo-khoa"))    return "MO_KHOA";
            if (uri.contains("/khoa"))       return "KHOA";
            if (uri.contains("/duyet"))      return "DUYET";
            if (uri.contains("/phan-quyen")) return "PHAN_QUYEN";
            if (uri.contains("/cap-tai-khoan")) return "PHAN_QUYEN";
            if (uri.contains("/import"))     return "IMPORT";
            if (uri.contains("/export"))     return "EXPORT";
        }

        // Fallback theo HTTP method
        if ("GET".equalsIgnoreCase(method))    return "XEM";
        if ("POST".equalsIgnoreCase(method))   return "TAO_MOI";
        if ("PUT".equalsIgnoreCase(method))    return "CAP_NHAT";
        if ("PATCH".equalsIgnoreCase(method))  return "CAP_NHAT";
        if ("DELETE".equalsIgnoreCase(method)) return "XOA";
        return method == null ? "KHAC" : method.toUpperCase();
    }

    private String xacDinhModule(String uri) {
        if (uri == null) return "KHAC";
        if (uri.contains("/sinh-vien")) return "SINH_VIEN";
        if (uri.contains("/dao-tao")) return "DAO_TAO";
        if (uri.contains("/chuong-trinh")) return "CHUONG_TRINH";
        if (uri.contains("/giang-day")) return "GIANG_DAY";
        if (uri.contains("/diem")) return "DIEM";
        if (uri.contains("/hoc-phi")) return "HOC_PHI";
        if (uri.contains("/thi")) return "THI";
        if (uri.contains("/lms")) return "LMS";
        if (uri.contains("/he-thong")) return "HE_THONG";
        return "KHAC";
    }

    private String xacDinhChucNang(String uri) {
        if (uri == null) return "KHAC";

        String[] parts = uri.split("/");

        if (parts.length >= 4) {
            return parts[3].replace("-", "_").toUpperCase();
        }

        return "KHAC";
    }

    private Long layBanGhiIdTuUri(String uri) {
        if (uri == null || uri.isBlank()) {
            return null;
        }

        String[] parts = uri.split("/");

        for (int i = parts.length - 1; i >= 0; i--) {
            String part = parts[i];

            if (part != null && part.matches("\\d+")) {
                try {
                    return Long.parseLong(part);
                } catch (NumberFormatException ignored) {
                    return null;
                }
            }
        }

        return null;
    }

    private String taoMoTa(
            String tenTaiKhoan,
            String taiKhoanEmail,
            String hanhDong,
            String module,
            String chucNang,
            String duongDan,
            String ketQua
    ) {
        String nguoiThaoTac = layGiaTri(tenTaiKhoan, taiKhoanEmail);

        return nullToBlank(nguoiThaoTac)
                + " thực hiện "
                + nullToBlank(hanhDong)
                + " ở module "
                + nullToBlank(module)
                + ", chức năng "
                + nullToBlank(chucNang)
                + ", đường dẫn "
                + nullToBlank(duongDan)
                + ", kết quả "
                + nullToBlank(ketQua);
    }

    private String taoDuLieuTimKiem(
            String taiKhoanEmail,
            String tenTaiKhoan,
            String doiTuongLoai,
            Long doiTuongId,
            String doiTuongTen,
            String module,
            String chucNang,
            String hanhDong,
            String ketQua,
            String duongDan,
            Long banGhiId,
            String moTa
    ) {
        StringBuilder builder = new StringBuilder();

        them(builder, taiKhoanEmail);
        them(builder, tenTaiKhoan);
        them(builder, doiTuongLoai);
        them(builder, doiTuongId == null ? null : String.valueOf(doiTuongId));
        them(builder, doiTuongTen);
        them(builder, module);
        them(builder, chucNang);
        them(builder, hanhDong);
        them(builder, ketQua);
        them(builder, duongDan);
        them(builder, banGhiId == null ? null : String.valueOf(banGhiId));
        them(builder, moTa);

        return builder.toString().toLowerCase();
    }

    private void them(StringBuilder builder, String value) {
        if (value == null || value.isBlank()) {
            return;
        }

        if (builder.length() > 0) {
            builder.append(" | ");
        }

        builder.append(value.trim());
    }

    private String layGiaTri(String value, String fallback) {
        if (value != null && !value.isBlank()) {
            return value;
        }

        return fallback;
    }

    private String nullToBlank(String value) {
        return value == null ? "" : value;
    }
}