package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.TrangThaiDiemDanh;

import java.time.*;
import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "diem_danh")
public class DiemDanh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lich_hoc_id", nullable = false)
    private Long lichHocId;

    @Column(name = "sinh_vien_id", nullable = false)
    private Long sinhVienId;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiDiemDanh trangThai;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "thoi_gian_diem_danh")
    private LocalDateTime thoiGianDiemDanh;

}
