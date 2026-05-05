package org.example.trungcapphuongnam.module.diem.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KetQuaLopHocPhanResponse {
    private Long id;

    private Long sinhVienId;

    private Long lopHocPhanId;

    private BigDecimal diemChuyenCan;

    private BigDecimal diemQuaTrinh;

    private BigDecimal diemThi;

    private BigDecimal diemTongKet;

    private BigDecimal diemPhanTram;

    private BigDecimal diemQuyDoi;

    private String ketQua;

    private String trangThai;

    private LocalDateTime ngayChot;

    private LocalDateTime ngayCongBo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
