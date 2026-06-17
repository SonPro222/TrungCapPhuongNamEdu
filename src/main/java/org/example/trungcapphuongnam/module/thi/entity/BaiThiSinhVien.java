package org.example.trungcapphuongnam.module.thi.entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.thi.enums.BaiThiSinhVienTrangThai;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bai_thi_sinh_vien", uniqueConstraints = @UniqueConstraint(columnNames = {"de_thi_id", "sinh_vien_id"}))
public class BaiThiSinhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "de_thi_id", nullable = false)
    private Long deThiId;

    @Column(name = "sinh_vien_id", nullable = false)
    private Long sinhVienId;

    @Column(name = "noi_dung_bai_lam", columnDefinition = "TEXT")
    private String noiDungBaiLam;

    @Column(name = "file_bai_lam", columnDefinition = "TEXT")
    private String fileBaiLam;

    @Column(name = "thoi_gian_bat_dau")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_nop")
    private LocalDateTime thoiGianNop;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private BaiThiSinhVienTrangThai trangThai;

    @PrePersist
    void prePersist() {
        if (trangThai == null) trangThai = BaiThiSinhVienTrangThai.dang_lam;
    }
}
