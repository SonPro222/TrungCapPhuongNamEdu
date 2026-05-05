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
@Table(name = "lop_hanh_chinh")
public class LopHanhChinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_lop", nullable = false, unique = true, length = 50)
    private String maLop;

    @Column(name = "ten_lop", nullable = false)
    private String tenLop;

    @Column(name = "chuong_trinh_version_id", nullable = false)
    private Long chuongTrinhVersionId;

    @Column(name = "khoa_dao_tao_id")
    private Long khoaDaoTaoId;

    @Column(name = "si_so")
    private Integer siSo;

    @Column(name = "trang_thai", length = 50)
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
        if (siSo == null) siSo = 0;
        if (trangThai == null) trangThai = "dang_hoc";
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
