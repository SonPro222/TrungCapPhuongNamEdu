package org.example.trungcapphuongnam.module.sinhVien.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienBaoLuuTrangThai;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sinh_vien_bao_luu")
public class SinhVienBaoLuu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sinh_vien_id", nullable = false)
    private SinhVien sinhVien;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sinh_vien_chuong_trinh_id", nullable = false)
    private SinhVienChuongTrinh sinhVienChuongTrinh;

    @Column(name = "chuong_trinh_version_id_cu", nullable = false)
    private Long chuongTrinhVersionIdCu;

    @Column(name = "lop_hanh_chinh_id_cu")
    private Long lopHanhChinhIdCu;

    @Column(name = "ngay_bat_dau", nullable = false)
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc_du_kien", nullable = false)
    private LocalDate ngayKetThucDuKien;

    @Column(name = "so_ky_bao_luu", nullable = false)
    private Integer soKyBaoLuu;

    @Column(name = "ngay_di_hoc_lai")
    private LocalDate ngayDiHocLai;

    @Column(name = "chuong_trinh_version_id_hoc_lai")
    private Long chuongTrinhVersionIdHocLai;

    @Column(name = "khung_ky_id_hoc_lai")
    private Long khungKyIdHocLai;

    @Column(name = "lop_hanh_chinh_id_hoc_lai")
    private Long lopHanhChinhIdHocLai;

    @Column(name = "ly_do", nullable = false, columnDefinition = "TEXT")
    private String lyDo;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, length = 50)
    private SinhVienBaoLuuTrangThai trangThai;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (trangThai == null) {
            trangThai = SinhVienBaoLuuTrangThai.dang_bao_luu;
        }

        if (ngayBatDau == null) {
            ngayBatDau = LocalDate.now();
        }

        if (soKyBaoLuu == null) {
            soKyBaoLuu = 1;
        }

        if (ngayKetThucDuKien == null) {
            ngayKetThucDuKien = ngayBatDau.plusMonths(soKyBaoLuu * 6L);
        }
    }
}