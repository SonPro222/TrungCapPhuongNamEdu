package org.example.trungcapphuongnam.module.lms.dto.response;

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
public class LanLamBaiOnlineResponse {

    private Long id;

    private Long baiTapLopId;

    private Long sinhVienId;

    private Integer lanThu;

    private LocalDateTime thoiGianBatDau;

    private LocalDateTime thoiGianNop;

    private BigDecimal diemTuDong;

    private BigDecimal diemGiaoVienCham;

    private BigDecimal diemCuoiCung;

    private Integer soCauDung;

    private Integer soCauSai;

    private Integer tongSoCau;

    private TrangThaiLanLamBai trangThai;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
