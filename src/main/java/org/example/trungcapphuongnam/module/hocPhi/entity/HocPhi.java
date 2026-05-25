package org.example.trungcapphuongnam.module.hocPhi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.hocPhi.enums.TrangThaiHocPhi;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hoc_phi")
public class HocPhi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sinh_vien_chuong_trinh_id", nullable = false)
    private Long sinhVienChuongTrinhId;

    @Column(name = "ten_khoan_thu")
    private String tenKhoanThu;

    @Column(name = "so_tien_phai_dong", nullable = false, precision = 14, scale = 2)
    private BigDecimal soTienPhaiDong;

    @Column(name = "so_tien_da_dong", precision = 14, scale = 2)
    private BigDecimal soTienDaDong;

    @Column(name = "han_dong")
    private LocalDate hanDong;

    @Column(name = "ngay_dong")
    private LocalDate ngayDong;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private TrangThaiHocPhi trangThai;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.soTienDaDong == null) {
            this.soTienDaDong = BigDecimal.ZERO;
        }
        if (this.trangThai == null) {
            this.trangThai = TrangThaiHocPhi.chua_dong;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
