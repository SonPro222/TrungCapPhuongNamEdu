package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.Data;

@Data
public class GiaoVienDangKyNhomCaRequest {
    private Long giaoVienId;
    private Long khungKyId;
    private Integer thuTrongTuan;
    private Long caBatDauId;
    private Long caKetThucId;
    private Integer mucDoUuTien;
    private String ghiChu;
}
