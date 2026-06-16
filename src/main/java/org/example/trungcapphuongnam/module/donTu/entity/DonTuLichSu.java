package org.example.trungcapphuongnam.module.donTu.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuHanhDong;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuTrangThai;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "don_tu_lich_su")
public class DonTuLichSu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "don_tu_id", nullable = false)
    private DonTu donTu;

    @Enumerated(EnumType.STRING)
    @Column(name = "hanh_dong", nullable = false, length = 100)
    private DonTuHanhDong hanhDong;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_cu", length = 50)
    private DonTuTrangThai trangThaiCu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_moi", length = 50)
    private DonTuTrangThai trangThaiMoi;

    @Column(name = "nguoi_thuc_hien_id")
    private Long nguoiThucHienId;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
