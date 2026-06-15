package org.example.trungcapphuongnam.module.daoTao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    @Column(name = "ngay_bat_dau_nhan_sinh_vien")
    private LocalDate ngayBatDauNhanSinhVien;

    @Column(name = "ngay_ket_thuc_nhan_sinh_vien")
    private LocalDate ngayKetThucNhanSinhVien;

    @Column(name = "da_chot_tuyen_sinh")
    private Boolean daChotTuyenSinh;

    @Column(name = "ngay_chot_tuyen_sinh")
    private LocalDateTime ngayChotTuyenSinh;

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
        if (daChotTuyenSinh == null) daChotTuyenSinh = false;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
