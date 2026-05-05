package org.example.trungcapphuongnam.module.sinhVien.entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.SinhVien.SinhVienChuongTrinhTrangThai;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "sinh_vien_chuong_trinh",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_sinh_vien_chuong_trinh_sv_ctv",
        columnNames = {"sinh_vien_id", "chuong_trinh_version_id"}
    )
)
public class SinhVienChuongTrinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sinh_vien_id", nullable = false)
    private SinhVien sinhVien;

    @Column(name = "chuong_trinh_version_id", nullable = false)
    private Long chuongTrinhVersionId;

    @Column(name = "lop_hanh_chinh_id")
    private Long lopHanhChinhId;

    @Column(name = "ngay_dang_ky")
    private LocalDate ngayDangKy;

    @Column(name = "ngay_nhap_hoc")
    private LocalDate ngayNhapHoc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private SinhVienChuongTrinhTrangThai trangThai;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (trangThai == null) {
            trangThai = SinhVienChuongTrinhTrangThai.da_dang_ky;
        }
    }
}
