package org.example.trungcapphuongnam.module.diem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "diem_chi_tiet")
public class DiemChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sinh_vien_id", nullable = false)
    private Long sinhVienId;

    @Column(name = "lop_hoc_phan_id", nullable = false)
    private Long lopHocPhanId;

    @Column(name = "cau_hinh_danh_gia_id")
    private Long cauHinhDanhGiaId;

    @Column(name = "bai_kiem_tra_id")
    private Long baiKiemTraId;

    @Column(name = "diem", precision = 5, scale = 2)
    private BigDecimal diem;

    @Column(name = "nhan_xet", columnDefinition = "TEXT")
    private String nhanXet;

    @Column(name = "giao_vien_cham_id")
    private Long giaoVienChamId;

    @Column(name = "ngay_cham")
    private LocalDateTime ngayCham;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (ngayCham == null) ngayCham = now;
        if (trangThai == null) trangThai = "da_nhap";
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
