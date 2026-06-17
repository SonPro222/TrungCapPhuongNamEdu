package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lich_hoc")
public class LichHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lop_hoc_phan_id", nullable = false)
    private Long lopHocPhanId;

    @Column(name = "giao_vien_id")
    private Long giaoVienId;

    @Column(name = "phong_hoc_id")
    private Long phongHocId;

    @Column(name = "ca_hoc_id")
    private Long caHocId;

    @Column(name = "ngay_hoc", nullable = false)
    private LocalDate ngayHoc;

    @Column(name = "noi_dung_buoi_hoc", columnDefinition = "TEXT")
    private String noiDungBuoiHoc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiLichHoc trangThai;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "vuot_khung_ky")
    private Boolean vuotKhungKy;

    @Column(name = "ngay_ket_thuc_goc")
    private LocalDate ngayKetThucGoc;

    @Column(name = "so_ngay_vuot_khung_ky")
    private Integer soNgayVuotKhungKy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
