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
        name = "nganh_loai_chuong_trinh",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_nganh_loai_chuong_trinh",
                        columnNames = {"nganh_id", "loai_chuong_trinh_id"}
                )
        }
)
public class NganhLoaiChuongTrinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nganh_id", nullable = false)
    private Long nganhId;

    @Column(name = "loai_chuong_trinh_id", nullable = false)
    private Long loaiChuongTrinhId;

    @Column(name = "trang_thai", nullable = false, length = 30)
    private String trangThai;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (trangThai == null || trangThai.trim().isEmpty()) {
            trangThai = "dang_su_dung";
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
