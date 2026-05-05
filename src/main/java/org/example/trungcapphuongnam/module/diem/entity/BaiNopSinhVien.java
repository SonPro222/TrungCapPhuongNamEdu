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
@Table(
        name = "bai_nop_sinh_vien",
        uniqueConstraints = @UniqueConstraint(name = "uk_bai_nop_sv", columnNames = {"bai_kiem_tra_id", "sinh_vien_id"})
)
public class BaiNopSinhVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bai_kiem_tra_id", nullable = false)
    private Long baiKiemTraId;

    @Column(name = "sinh_vien_id", nullable = false)
    private Long sinhVienId;

    @Column(name = "noi_dung_bai_lam", columnDefinition = "TEXT")
    private String noiDungBaiLam;

    @Column(name = "file_dinh_kem", columnDefinition = "TEXT")
    private String fileDinhKem;

    @Column(name = "thoi_gian_nop")
    private LocalDateTime thoiGianNop;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;

    @PrePersist
    public void prePersist() {
        if (thoiGianNop == null) thoiGianNop = LocalDateTime.now();
        if (trangThai == null) trangThai = "da_nop";
    }
}
