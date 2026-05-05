package org.example.trungcapphuongnam.module.heThong.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.TrangThaiNhanVien;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "nhan_vien",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_nhan_vien_tai_khoan", columnNames = "tai_khoan_id"),
                @UniqueConstraint(name = "uk_nhan_vien_ma_nhan_vien", columnNames = "ma_nhan_vien")
        }
)
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tai_khoan_id", unique = true)
    private TaiKhoan taiKhoan;

    @Column(name = "ma_nhan_vien", nullable = false, unique = true, length = 50)
    private String maNhanVien;

    @Column(name = "ho_ten", nullable = false, length = 255)
    private String hoTen;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "so_dien_thoai", length = 50)
    private String soDienThoai;

    @Column(name = "phong_ban", length = 100)
    private String phongBan;

    @Column(name = "chuc_vu", length = 100)
    private String chucVu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private TrangThaiNhanVien trangThai;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}