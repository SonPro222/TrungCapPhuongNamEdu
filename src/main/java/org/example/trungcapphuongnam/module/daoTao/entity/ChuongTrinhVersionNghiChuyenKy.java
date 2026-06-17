package org.example.trungcapphuongnam.module.daoTao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "chuong_trinh_version_nghi_chuyen_ky",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_ctv_nghi_chuyen_ky",
                        columnNames = {"chuong_trinh_version_id", "tu_ky_thu", "den_ky_thu"}
                )
        }
)
public class ChuongTrinhVersionNghiChuyenKy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_version_id", nullable = false)
    private Long chuongTrinhVersionId;

    @Column(name = "tu_ky_thu", nullable = false)
    private Integer tuKyThu;

    @Column(name = "den_ky_thu", nullable = false)
    private Integer denKyThu;

    @Column(name = "so_ngay_nghi", nullable = false)
    private Integer soNgayNghi;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
        if (soNgayNghi == null) {
            soNgayNghi = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        if (soNgayNghi == null) {
            soNgayNghi = 0;
        }
    }
}
