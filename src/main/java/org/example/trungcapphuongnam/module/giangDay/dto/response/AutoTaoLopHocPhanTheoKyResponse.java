package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AutoTaoLopHocPhanTheoKyResponse {

    private Long chuongTrinhVersionId;
    private Long khungKyId;

    /** Mode đã thực thi: PREVIEW / DU_KIEN / CHINH_THUC */
    private String mode;

    /** Tổng sinh viên (từ các lớp hành chính đã chọn) */
    private Integer tongSinhVien;

    /** Tổng số môn trong kỳ (từ ChuongTrinhMon) */
    private Integer tongMonTrongKy;

    /** Tổng lớp học phần dự kiến cần tạo */
    private Integer tongLopHocPhanDuKien;

    /** Tổng lớp học phần thực sự đã tạo/cập nhật (0 nếu mode = PREVIEW) */
    private Integer tongLopHocPhanDaTao;

    private List<AutoTaoLopHocPhanMonResult> monResults;

    /** Cảnh báo tổng hợp (không block) */
    private List<String> canhBao;

    /** Lỗi tổng hợp (các môn bị bỏ qua) */
    private List<String> loi;
}
