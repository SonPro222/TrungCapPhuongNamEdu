package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "giang_vien_dang_ky_giang_day")
public class GiangVienDangKyGiangDay {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "giao_vien_id", nullable = false)
    private Long giaoVienId;

    @Column(name = "khung_ky_id", nullable = false)
    private Long khungKyId;

    @Column(name = "so_tiet_dang_ky", nullable = false)
    private Integer soTietDangKy;

    @Column(name = "so_tiet_da_phan_cong", nullable = false)
    private Integer soTietDaPhanCong;

    @Column(name = "trang_thai", nullable = false)
    private String trangThai;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
