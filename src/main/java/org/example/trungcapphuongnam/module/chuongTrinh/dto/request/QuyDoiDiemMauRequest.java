package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.example.trungcapphuongnam.module.chuongTrinh.constant.KetQuaDatKhongDat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuyDoiDiemMauRequest {

    private String ma;
    private String ten;
    private BigDecimal nguongTu;
    private BigDecimal nguongDen;
    private BigDecimal diemQuyDoi;
    private KetQuaDatKhongDat ketQua;
    private String congThuc;
    private String ghiChu;
}
