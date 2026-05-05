package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Lms.KetQuaDat;
import org.example.trungcapphuongnam.common.enums.Lms.TrangThaiKetQuaBaiTap;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ket_qua_bai_tap_lop")
public class KetQuaBaiTapLop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bai_tap_lop_id")
    private Long baiTapLopId;

    @Column(name = "sinh_vien_id")
    private Long sinhVienId;

    @Column(name = "lan_lam_duoc_chon_id")
    private Long lanLamDuocChonId;

    @Column(name = "diem_he_thong")
    private BigDecimal diemHeThong;

    @Column(name = "diem_giao_vien_chinh_sua")
    private BigDecimal diemGiaoVienChinhSua;

    @Column(name = "diem_cuoi_cung")
    private BigDecimal diemCuoiCung;

    @Enumerated(EnumType.STRING)
    @Column(name = "ket_qua")
    private KetQuaDat ketQua;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiKetQuaBaiTap trangThai;

    @Column(name = "diem_chi_tiet_id")
    private Long diemChiTietId;

    @Column(name = "nhan_xet")
    private String nhanXet;

    @Column(name = "ngay_tinh_diem")
    private LocalDateTime ngayTinhDiem;

    @Column(name = "ngay_chot")
    private LocalDateTime ngayChot;

    @Column(name = "nguoi_chot_id")
    private Long nguoiChotId;

    @Column(name = "ngay_day_sang_diem")
    private LocalDateTime ngayDaySangDiem;

    @Column(name = "nguoi_day_sang_diem_id")
    private Long nguoiDaySangDiemId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
