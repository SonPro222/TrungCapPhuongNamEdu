package org.example.trungcapphuongnam.module.diem.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHinhDanhGiaResponse {
    private Long id;

    private Long syllabusMonHocId;

    /** Chỉ trả kèm khi BE resolve từ lopHocPhanId filter cũ; DB không còn lưu cột này. */
    private Long lopHocPhanId;

    private String tenCotDiem;

    private String loaiDiem;

    /** Deprecated, luôn null ở flow mới. */
    private String nguonDiem;

    private BigDecimal tyLe;

    private BigDecimal diemToiDa;

    private Integer thuTu;

    private Boolean batBuoc;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
