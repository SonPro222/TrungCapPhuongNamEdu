package org.example.trungcapphuongnam.module.donTu.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiemDanhLichSuResponse {

    private Long id;
    private Long diemDanhId;
    private Long donTuId;
    private TrangThaiDiemDanh trangThaiCu;
    private TrangThaiDiemDanh trangThaiMoi;
    private String ghiChuCu;
    private String ghiChuMoi;
    private Long nguoiThucHienId;
    private String lyDo;
    private LocalDateTime createdAt;
}
