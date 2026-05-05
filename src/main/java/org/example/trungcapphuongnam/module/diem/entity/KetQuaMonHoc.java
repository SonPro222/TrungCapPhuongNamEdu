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
        name = "ket_qua_mon_hoc",
        uniqueConstraints = @UniqueConstraint(name = "uk_kq_mon_sv_lan", columnNames = {"sinh_vien_id", "chuong_trinh_mon_id", "so_lan_hoc"})
)
public class KetQuaMonHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sinh_vien_id", nullable = false)
    private Long sinhVienId;

    @Column(name = "chuong_trinh_mon_id", nullable = false)
    private Long chuongTrinhMonId;

    @Column(name = "lop_hoc_phan_id")
    private Long lopHocPhanId;

    @Column(name = "diem_tong_ket", precision = 5, scale = 2)
    private BigDecimal diemTongKet;

    @Column(name = "diem_phan_tram", precision = 5, scale = 2)
    private BigDecimal diemPhanTram;

    @Column(name = "diem_quy_doi", precision = 5, scale = 2)
    private BigDecimal diemQuyDoi;

    @Column(name = "ket_qua", length = 50)
    private String ketQua;

    @Column(name = "so_lan_hoc")
    private Integer soLanHoc;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        ngayCapNhat = now;
        if (soLanHoc == null) soLanHoc = 1;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        ngayCapNhat = updatedAt;
    }
}
