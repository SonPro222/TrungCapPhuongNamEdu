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
@Table(
        name = "ket_qua_lop_hoc_phan",
        uniqueConstraints = @UniqueConstraint(name = "uk_kq_lop_sv", columnNames = {"sinh_vien_id", "lop_hoc_phan_id"})
)
public class KetQuaLopHocPhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sinh_vien_id", nullable = false)
    private Long sinhVienId;

    @Column(name = "lop_hoc_phan_id", nullable = false)
    private Long lopHocPhanId;

    @Column(name = "diem_chuyen_can", precision = 5, scale = 2)
    private BigDecimal diemChuyenCan;

    @Column(name = "diem_qua_trinh", precision = 5, scale = 2)
    private BigDecimal diemQuaTrinh;

    @Column(name = "diem_thi", precision = 5, scale = 2)
    private BigDecimal diemThi;

    @Column(name = "diem_tong_ket", precision = 5, scale = 2)
    private BigDecimal diemTongKet;

    @Column(name = "diem_phan_tram", precision = 5, scale = 2)
    private BigDecimal diemPhanTram;

    @Column(name = "diem_quy_doi", precision = 5, scale = 2)
    private BigDecimal diemQuyDoi;

    @Column(name = "ket_qua", length = 50)
    private String ketQua;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @Column(name = "ngay_chot")
    private LocalDateTime ngayChot;

    @Column(name = "ngay_cong_bo")
    private LocalDateTime ngayCongBo;

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
