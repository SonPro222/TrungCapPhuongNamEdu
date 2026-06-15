package org.example.trungcapphuongnam.module.giangDay.enums;

/**
 * Mode tự động tạo lớp học phần theo kỳ.
 */
public enum AutoTaoMode {
    /**
     * Chỉ tính toán, không ghi DB.
     * Trả về số lớp dự kiến, sĩ số, cảnh báo.
     */
    PREVIEW,

    /**
     * Tạo/cập nhật lớp học phần trạng thái {@code du_kien}.
     * Phân bổ sinh viên dự kiến.
     * Cho phép chạy lại nếu lớp chưa có dữ liệu sâu.
     */
    DU_KIEN,

    /**
     * Chốt lớp học phần chính thức, chuyển trạng thái sang {@code dang_mo}.
     * Yêu cầu {@code choPhepMoDuKien = true} để xác nhận kỳ tuyển sinh đã kết thúc.
     */
    CHINH_THUC
}
