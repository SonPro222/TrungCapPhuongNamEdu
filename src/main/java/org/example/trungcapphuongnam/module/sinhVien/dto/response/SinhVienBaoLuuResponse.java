package org.example.trungcapphuongnam.module.sinhVien.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienBaoLuuTrangThai;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVienBaoLuuResponse {

    private Long id;

    private Long sinhVienId;
    private String maSinhVien;
    private String hoTen;

    private Long sinhVienChuongTrinhId;

    private Long chuongTrinhVersionIdCu;
    private Long lopHanhChinhIdCu;

    private LocalDate ngayBatDau;
    private LocalDate ngayKetThucDuKien;
    private Integer soKyBaoLuu;

    private LocalDate ngayDiHocLai;
    private Long chuongTrinhVersionIdHocLai;
    private Long khungKyIdHocLai;
    private Long lopHanhChinhIdHocLai;

    private String lyDo;
    private SinhVienBaoLuuTrangThai trangThai;
    private String ghiChu;

    private Integer soMonDuocCongNhan;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}