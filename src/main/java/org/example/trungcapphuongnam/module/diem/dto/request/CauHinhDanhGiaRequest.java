package org.example.trungcapphuongnam.module.diem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHinhDanhGiaRequest {
    @NotNull(message = "syllabusMonHocId không được để trống")
    private Long syllabusMonHocId;

    /** Chỉ giữ để tương thích request cũ; nghiệp vụ mới không lưu theo lớp học phần. */
    private Long lopHocPhanId;

    @NotBlank(message = "tenCotDiem không được để trống")
    private String tenCotDiem;

    @NotBlank(message = "loaiDiem không được để trống")
    private String loaiDiem;

    /** Deprecated: cột điểm thuộc syllabus, nguồn điểm do bài kiểm tra/LMS xử lý riêng. */
    private String nguonDiem;

    @NotNull(message = "tyLe không được để trống")
    @DecimalMin(value = "0.0", message = "tyLe không được âm")
    @DecimalMax(value = "100.0", message = "tyLe không được vượt quá 100")
    private BigDecimal tyLe;

    @DecimalMin(value = "0.0", message = "diemToiDa không được âm")
    private BigDecimal diemToiDa;

    private Integer thuTu;

    private Boolean batBuoc;

    private String ghiChu;
}
