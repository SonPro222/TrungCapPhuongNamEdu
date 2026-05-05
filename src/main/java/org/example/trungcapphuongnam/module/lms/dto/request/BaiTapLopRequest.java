package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Lms.CachTinhDiem;
import org.example.trungcapphuongnam.common.enums.Lms.TrangThaiBaiTapLop;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiTapLopRequest {

    @NotNull

    private Long dotGiaoBaiOnlineId;

    @NotNull

    private Long baiTapOnlineId;

    @NotNull

    private Long lopHocPhanId;

    @NotNull

    private Long cauHinhDanhGiaId;

    @NotNull

    private Long nguoiGiaoTaiKhoanId;

    @NotNull

    private LocalDateTime thoiGianMo;

    @NotNull

    private LocalDateTime thoiGianDong;

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

}
