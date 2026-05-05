package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionResponse {

    private Long id;

    private Long chuongTrinhId;

    private String maVersion;

    private String tenVersion;

    private LocalDate ngayApDung;

    private LocalDate ngayHetHieuLuc;

    private String soQuyetDinh;

    private LocalDate ngayQuyetDinh;

    private String nguoiKy;

    private String coQuanBanHanh;

    private String fileQuyetDinh;

    private BigDecimal tongTinChi;

    private BigDecimal tongSoGio;

    private BigDecimal tongGioLyThuyet;

    private BigDecimal tongGioThucHanh;

    private BigDecimal tongGioKiemTra;

    private Boolean laHienHanh;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
