package org.example.trungcapphuongnam.module.lms.dto.response;

import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiGoc;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHoiGocResponse {
    private Long id;

    private Long monHocId;

    private String maCauHoi;

    private Long nguoiTaoTaiKhoanId;

    private TrangThaiCauHoiGoc trangThai;

    private Long donViSoHuuId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
