package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GiaoVienDangKyNhomCaResponse {
    private Long id;
    private Long giaoVienId;
    private String maGiaoVien;
    private String hoTenGiaoVien;
    private Long khungKyId;
    private String maKy;
    private String tenKy;
    private Integer thuTrongTuan;
    private Long caBatDauId;
    private String tenCaBatDau;
    private Integer thuTuBatDau;
    private Long caKetThucId;
    private String tenCaKetThuc;
    private Integer thuTuKetThuc;
    private Integer mucDoUuTien;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
