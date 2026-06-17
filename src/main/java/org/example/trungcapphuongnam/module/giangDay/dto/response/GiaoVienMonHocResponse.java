package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GiaoVienMonHocResponse {
    private Long id;
    private Long giaoVienId;
    private String maGiaoVien;
    private String hoTenGiaoVien;
    private Long monHocId;
    private String maMon;
    private String tenMon;
    private String trangThai;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
