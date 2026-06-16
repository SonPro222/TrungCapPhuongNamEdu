package org.example.trungcapphuongnam.module.donTu.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonTuDuyetKhoiPhucDiemDanhRequest {

    private Long nguoiThucHienId;

    private Long diemDanhId;

    private TrangThaiDiemDanh trangThaiMoi;

    private String ghiChuMoi;

    private String yKienXuLy;

    private String lyDo;
}
