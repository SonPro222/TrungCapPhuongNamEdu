package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GiangVienDangKyGiangDayResponse {
    private Long id;
    private Long giaoVienId;
    private String maGiaoVien;
    private String hoTenGiaoVien;
    private Long khungKyId;
    private String maKy;
    private String tenKy;
    private Integer soTietDangKy;
    private Integer soTietDaPhanCong;
    private Integer soTietConLai;
    private String trangThai;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Enriched khung kỳ — ngữ cảnh đầy đủ
    private Long chuongTrinhVersionId;
    private String tenVersion;
    private Long chuongTrinhId;
    private String tenChuongTrinh;
    private Long nganhId;
    private String tenNganh;
    private String labelKhungKyDayDu;
}
