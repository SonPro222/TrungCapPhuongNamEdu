package org.example.trungcapphuongnam.module.sinhVien.dto.response;


import lombok.*;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienChuongTrinhTrangThai;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVienChuongTrinhResponse {
    private Long id;
    private Long sinhVienId;
    private String maSinhVien;
    private String hoTen;
    private Long chuongTrinhVersionId;
    private Long lopHanhChinhId;
    private LocalDate ngayDangKy;
    private LocalDate ngayNhapHoc;
    private SinhVienChuongTrinhTrangThai trangThai;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
