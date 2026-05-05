package org.example.trungcapphuongnam.module.diem.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KetQuaMonHocResponse {
    private Long id;

    private Long sinhVienId;

    private Long chuongTrinhMonId;

    private Long lopHocPhanId;

    private BigDecimal diemTongKet;

    private BigDecimal diemPhanTram;

    private BigDecimal diemQuyDoi;

    private String ketQua;

    private Integer soLanHoc;

    private LocalDateTime ngayCapNhat;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
