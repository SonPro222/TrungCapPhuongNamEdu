package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GiangVienDangKyGiangDayRequest {
    private Long giaoVienId;
    private Long khungKyId;
    private Integer soTietDangKy;
    private String trangThai;
    private String ghiChu;
}
