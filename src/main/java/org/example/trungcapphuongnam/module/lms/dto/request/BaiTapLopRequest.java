package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.CachTinhDiem;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiBaiTapLop;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiTapLopRequest {
    private Long dotGiaoBaiOnlineId;

    @NotNull
    private Long baiTapOnlineId;

    @NotNull
    private Long lopHocPhanId;

    private Long cauHinhDanhGiaId;
    private Long nguoiGiaoTaiKhoanId;

    @NotNull
    private LocalDateTime thoiGianMo;

    @NotNull
    private LocalDateTime thoiGianDong;

    @Min(1)
    private Integer thoiLuongLamBaiPhut;

    @Min(1)
    private Integer soLanLamToiDa;

    private CachTinhDiem cachTinhDiem;
    private Boolean choPhepXemDiem;
    private Boolean choPhepXemDapAn;
    private Boolean tronCauHoi;
    private Boolean tronDapAn;
    private Boolean choPhepLamQuaHan;
    private Boolean giaoVienDuocChinhSua;
    private TrangThaiBaiTapLop trangThai;
    private String ghiChu;

    @DecimalMin(value = "0")
    private BigDecimal diemToiDa;

    private Boolean yeuCauToanManHinh;

    @Min(0)
    private Integer gioiHanRoiTab;

    private Boolean tuDongNopKhiViPham;
    private String matKhauLamBai;
}
