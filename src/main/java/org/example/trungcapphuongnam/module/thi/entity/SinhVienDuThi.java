package org.example.trungcapphuongnam.module.thi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Thi.SinhVienDuThiTrangThai;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sinh_vien_du_thi", uniqueConstraints = @UniqueConstraint(columnNames = {"lich_thi_id", "sinh_vien_id"}))
public class SinhVienDuThi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lich_thi_id", nullable = false)
    private Long lichThiId;

    @Column(name = "sinh_vien_id", nullable = false)
    private Long sinhVienId;

    @Column(name = "du_dieu_kien_thi")
    private Boolean duDieuKienThi;

    @Column(name = "ly_do_khong_du_dieu_kien", columnDefinition = "TEXT")
    private String lyDoKhongDuDieuKien;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private SinhVienDuThiTrangThai trangThai;

    @PrePersist
    void prePersist() {
        if (duDieuKienThi == null) duDieuKienThi = true;
        if (trangThai == null) trangThai = SinhVienDuThiTrangThai.du_kien;
    }
}
