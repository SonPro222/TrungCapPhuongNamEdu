package org.example.trungcapphuongnam.module.diem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bai_kiem_tra")
public class BaiKiemTra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lop_hoc_phan_id", nullable = false)
    private Long lopHocPhanId;

    @Column(name = "cau_hinh_danh_gia_id")
    private Long cauHinhDanhGiaId;

    @Column(name = "giao_vien_id")
    private Long giaoVienId;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @Column(name = "loai_bai", nullable = false, length = 50)
    private String loaiBai;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "noi_dung_de", columnDefinition = "TEXT")
    private String noiDungDe;

    @Column(name = "thoi_gian_bat_dau")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc")
    private LocalDateTime thoiGianKetThuc;

    @Column(name = "hinh_thuc", length = 50)
    private String hinhThuc;

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
        if (trangThai == null) trangThai = "nhap";
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
