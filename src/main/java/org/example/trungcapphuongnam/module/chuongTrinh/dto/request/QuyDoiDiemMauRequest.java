package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;

import java.math.BigDecimal;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.KetQuaDatKhongDat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuyDoiDiemMauRequest {

    private Long syllabusMonHocMauId;
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

}
