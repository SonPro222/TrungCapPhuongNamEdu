package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "diem_danh_lich_su")
public class DiemDanhLichSu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "diem_danh_id", nullable = false)
    private DiemDanh diemDanh;

    @Column(name = "don_tu_id")
    private Long donTuId;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_cu", length = 50)
    private TrangThaiDiemDanh trangThaiCu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_moi", length = 50)
    private TrangThaiDiemDanh trangThaiMoi;

    @Column(name = "ghi_chu_cu", columnDefinition = "TEXT")
    private String ghiChuCu;

    @Column(name = "ghi_chu_moi", columnDefinition = "TEXT")
    private String ghiChuMoi;

    @Column(name = "nguoi_thuc_hien_id")
    private Long nguoiThucHienId;

    @Column(name = "ly_do", columnDefinition = "TEXT")
    private String lyDo;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
