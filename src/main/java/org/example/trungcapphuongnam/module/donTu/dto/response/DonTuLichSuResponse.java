package org.example.trungcapphuongnam.module.donTu.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuHanhDong;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuTrangThai;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonTuLichSuResponse {

    private Long id;
    private Long donTuId;
    private DonTuHanhDong hanhDong;
    private DonTuTrangThai trangThaiCu;
    private DonTuTrangThai trangThaiMoi;
    private Long nguoiThucHienId;
    private String ghiChu;
    private LocalDateTime createdAt;
}
