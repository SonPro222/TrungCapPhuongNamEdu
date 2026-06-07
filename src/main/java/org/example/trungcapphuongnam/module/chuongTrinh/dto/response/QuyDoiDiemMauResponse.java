package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.KetQuaDatKhongDat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuyDoiDiemMauResponse {

    private Long syllabusMonHocGocId;
    private Long id;
    private String ma;
    private String ten;
    private BigDecimal nguongTu;
    private BigDecimal nguongDen;
    private BigDecimal diemQuyDoi;
    private KetQuaDatKhongDat ketQua;
    private String congThuc;
    private String ghiChu;
    private String loaiMau;
    private BigDecimal tyLe;
    private BigDecimal diemToiDa;
    private Integer thuTu;
    private Boolean batBuoc;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
