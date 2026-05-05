package org.example.trungcapphuongnam.module.sinhVien.entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.SinhVien.GioiTinh;
import org.example.trungcapphuongnam.common.enums.SinhVien.SinhVienTrangThai;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sinh_vien")
public class SinhVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_sinh_vien", nullable = false, unique = true, length = 50)
    private String maSinhVien;

    @Column(name = "ho_ten", nullable = false, length = 255)
    private String hoTen;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Enumerated(EnumType.STRING)
    @Column(name = "gioi_tinh", length = 20)
    private GioiTinh gioiTinh;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "so_dien_thoai", length = 50)
    private String soDienThoai;

    @Column(name = "dia_chi", columnDefinition = "TEXT")
    private String diaChi;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private SinhVienTrangThai trangThai;

    @Column(name = "tai_khoan_id", unique = true)
    private Long taiKhoanId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (trangThai == null) {
            trangThai = SinhVienTrangThai.dang_hoc;
        }
    }
}
