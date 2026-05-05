package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Lms.TrangThaiLanLamBai;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lan_lam_bai_online")
public class LanLamBaiOnline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bai_tap_lop_id")
    private Long baiTapLopId;

    @Column(name = "sinh_vien_id")
    private Long sinhVienId;

    @Column(name = "lan_thu")
    private Integer lanThu;

    @Column(name = "thoi_gian_bat_dau")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_nop")
    private LocalDateTime thoiGianNop;

    @Column(name = "diem_tu_dong")
    private BigDecimal diemTuDong;

    @Column(name = "diem_giao_vien_cham")
    private BigDecimal diemGiaoVienCham;

    @Column(name = "diem_cuoi_cung")
    private BigDecimal diemCuoiCung;

    @Column(name = "so_cau_dung")
    private Integer soCauDung;

    @Column(name = "so_cau_sai")
    private Integer soCauSai;

    @Column(name = "tong_so_cau")
    private Integer tongSoCau;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiLanLamBai trangThai;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
