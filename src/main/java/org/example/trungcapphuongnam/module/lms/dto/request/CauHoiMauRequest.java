package org.example.trungcapphuongnam.module.lms.dto.request;

import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiMau;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHoiMauRequest {
    private Long monHocId;

    private String maCauHoi;

    private Long nguoiTaoTaiKhoanId;

    private TrangThaiCauHoiMau trangThai;

    private Long donViSoHuuId;

}
