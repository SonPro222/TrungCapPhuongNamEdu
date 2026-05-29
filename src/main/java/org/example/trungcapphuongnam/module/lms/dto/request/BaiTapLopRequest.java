package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
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


    @NotNull

    private Long baiTapOnlineId;

    @NotNull

    private Long lopHocPhanId;


    @NotNull

    private LocalDateTime thoiGianMo;

    private Long dotGiaoBaiOnlineId;

    private Long cauHinhDanhGiaId;

    private Long nguoiGiaoTaiKhoanId;

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
