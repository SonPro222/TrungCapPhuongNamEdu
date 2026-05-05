package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Lms.TrangThaiLanLamBai;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanLamBaiOnlineRequest {

    @NotNull

    private Long baiTapLopId;

    @NotNull

    private Long sinhVienId;

    @Min(1)

    private Integer lanThu;

    private LocalDateTime thoiGianBatDau;

    private LocalDateTime thoiGianNop;

    @DecimalMin(value = "0")

    private BigDecimal diemTuDong;

    @DecimalMin(value = "0")

    private BigDecimal diemGiaoVienCham;

    @DecimalMin(value = "0")

    private BigDecimal diemCuoiCung;

    private Integer soCauDung;

    private Integer soCauSai;

    private Integer tongSoCau;

    private TrangThaiLanLamBai trangThai;

    private String ghiChu;

}
