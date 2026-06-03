package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiGoc;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHoiGocRequest {
    private Long monHocId;

    private String maCauHoi;

    private Long nguoiTaoTaiKhoanId;

    private TrangThaiCauHoiGoc trangThai;

    private Long donViSoHuuId;

}
