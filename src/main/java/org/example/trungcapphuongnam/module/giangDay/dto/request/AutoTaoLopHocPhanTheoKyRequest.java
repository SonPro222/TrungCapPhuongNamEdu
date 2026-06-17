package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.Data;
import org.example.trungcapphuongnam.module.giangDay.enums.AutoTaoMode;

import java.util.List;

@Data
public class AutoTaoLopHocPhanTheoKyRequest {

    /** ID của ChuongTrinhVersion đang triển khai */
    private Long chuongTrinhVersionId;

    /** ID của KhungKy (kỳ học) cần mở lớp */
    private Long khungKyId;

    /** Danh sách lớp hành chính tham gia kỳ này (để lấy sinh viên). Null/empty = lấy tất cả SV của version */
    private List<Long> lopHanhChinhIds;

    /** Sĩ số tối thiểu mỗi lớp học phần */
    private Integer siSoToiThieu;

    /** Sĩ số tối đa mỗi lớp học phần */
    private Integer siSoToiDa;

    // ngayBatDau/ngayKetThuc đã bỏ: thời gian học phần thuộc lịch học, không phải LHP

    /** Tự động phân bổ sinh viên vào lớp hay không */
    private Boolean tuDongPhanBoSinhVien;

    /**
     * Xác nhận kỳ tuyển sinh đã kết thúc / danh sách đã chốt.
     * Bắt buộc = true khi mode = CHINH_THUC.
     */
    private Boolean choPhepMoDuKien;

    /**
     * Mode tự động tạo lớp:
     * PREVIEW = chỉ tính toán, DU_KIEN = tạo dự kiến, CHINH_THUC = chốt chính thức.
     */
    private AutoTaoMode mode;

    /** Tiền tố mã lớp, ví dụ "LHP" → "LHP-JAVA-01". Mặc định "LHP" nếu null */
    private String tienToMaLop;
}
