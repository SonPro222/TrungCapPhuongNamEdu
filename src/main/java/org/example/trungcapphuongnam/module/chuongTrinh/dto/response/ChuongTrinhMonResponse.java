package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhMonResponse {

    private Long id;

    private Long chuongTrinhVersionId;

    private Long monHocId;

    private String maMonTrongCt;

    private Long khungKyId;

    private Long nhomKienThucId;

    private String loai;

    private String loaiHocPhan;
    private String loaiPhamVi;
    private Boolean batBuoc;

    private Boolean laMonDieuKien;

    private Integer thuTu;

    private BigDecimal soTinChi;

    private BigDecimal tongGio;

    private BigDecimal gioLyThuyet;

    private BigDecimal gioThucHanh;

    private BigDecimal gioKiemTra;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
