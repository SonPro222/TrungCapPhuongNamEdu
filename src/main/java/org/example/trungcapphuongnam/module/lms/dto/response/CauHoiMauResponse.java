package org.example.trungcapphuongnam.module.lms.dto.response;

import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiMau;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHoiMauResponse {
    private Long id;

    private Long monHocId;

    private String maCauHoi;

    private Long nguoiTaoTaiKhoanId;

    private TrangThaiCauHoiMau trangThai;

    private Long donViSoHuuId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
