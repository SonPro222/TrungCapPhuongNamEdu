package org.example.trungcapphuongnam.module.heThong.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHinhMaSinhVienResponse {

    private Long id;

    private Long nganhId;
    private String maNganh;
    private String tenNganh;

    private Long chuongTrinhVersionId;
    private String maVersion;
    private String tenVersion;

    private String tienTo;
    private String maDau;
    private Integer soHienTai;
    private Integer doDaiSoThuTu;
    private String ghiChu;

    private String maSinhVienTiepTheo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}