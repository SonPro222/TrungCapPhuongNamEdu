package org.example.trungcapphuongnam.module.heThong.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.LoaiTaiKhoan;
import org.example.trungcapphuongnam.common.enums.TrangThaiTaiKhoan;


import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "tai_khoan",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_tai_khoan_email", columnNames = "email")
        }
)
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "mat_khau_hash", columnDefinition = "TEXT")
    private String matKhauHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_tai_khoan", nullable = false, length = 50)
    private LoaiTaiKhoan loaiTaiKhoan;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private TrangThaiTaiKhoan trangThai;

    @Column(name = "lan_dang_nhap_cuoi")
    private LocalDateTime lanDangNhapCuoi;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "taiKhoan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TaiKhoanVaiTro> taiKhoanVaiTros = new LinkedHashSet<>();
}