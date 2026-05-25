package org.example.trungcapphuongnam.module.lms.dto.response;

import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.CachTinhDiem;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiBaiTapLop;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiTapLopResponse {

    private Long id;

    private Long dotGiaoBaiOnlineId;

    private Long baiTapOnlineId;

    private Long lopHocPhanId;

    private Long cauHinhDanhGiaId;

    private Long nguoiGiaoTaiKhoanId;

    private LocalDateTime thoiGianMo;

    private LocalDateTime thoiGianDong;

    private Integer thoiLuongLamBaiPhut;

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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
