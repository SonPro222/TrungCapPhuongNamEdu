package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionRequest {

    @NotNull(message = "chuong_trinh_id không được để trống")
    private Long chuongTrinhId;

    @NotBlank(message = "ma_version không được để trống")
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
