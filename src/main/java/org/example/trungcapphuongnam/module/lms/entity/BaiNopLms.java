package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiBaiNopLms;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bai_nop_lms")
public class BaiNopLms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bai_tap_lop_id", nullable = false)
    private Long baiTapLopId;

    @Column(name = "sinh_vien_id", nullable = false)
    private Long sinhVienId;

    @Column(name = "lan_nop", nullable = false)
    private Integer lanNop;

    @Column(name = "noi_dung_bai_lam", columnDefinition = "TEXT")
    private String noiDungBaiLam;

    @Column(name = "thoi_gian_nop")
    private LocalDateTime thoiGianNop;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiBaiNopLms trangThai;

    @Column(name = "diem")
    private BigDecimal diem;

    @Column(name = "nhan_xet", columnDefinition = "TEXT")
    private String nhanXet;

    @Column(name = "nguoi_cham_id")
    private Long nguoiChamId;

    @Column(name = "thoi_gian_cham")
    private LocalDateTime thoiGianCham;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
