package org.example.trungcapphuongnam.module.donTu.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonTuDuyetBaoLuuRequest {

    private Long nguoiThucHienId;

    private Long sinhVienChuongTrinhId;

    private LocalDate ngayBatDau;

    private LocalDate ngayKetThucDuKien;

    private Integer soKyBaoLuu;

    private String lyDo;

    private String ghiChu;

    private String yKienXuLy;
}
