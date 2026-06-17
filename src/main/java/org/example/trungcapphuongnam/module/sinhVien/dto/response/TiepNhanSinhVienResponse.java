package org.example.trungcapphuongnam.module.sinhVien.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TiepNhanSinhVienResponse {
    private SinhVienResponse sinhVien;
    private Long sinhVienChuongTrinhId;
    private Long nganhId;
    private Long chuongTrinhId;
    private Long chuongTrinhVersionId;
    private String emailTaiKhoan;
    private String matKhauTam;
}
