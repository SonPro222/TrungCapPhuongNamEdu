package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.CachTinhDiem;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiBaiTapLop;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bai_tap_lop")
public class BaiTapLop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dot_giao_bai_online_id")
    private Long dotGiaoBaiOnlineId;

    @Column(name = "bai_tap_online_id", nullable = false)
    private Long baiTapOnlineId;

    @Column(name = "lop_hoc_phan_id", nullable = false)
    private Long lopHocPhanId;

    @Column(name = "cau_hinh_danh_gia_id")
    private Long cauHinhDanhGiaId;

    @Column(name = "nguoi_giao_tai_khoan_id")
    private Long nguoiGiaoTaiKhoanId;

    @Column(name = "thoi_gian_mo", nullable = false)
    private LocalDateTime thoiGianMo;

    @Column(name = "thoi_gian_dong", nullable = false)
    private LocalDateTime thoiGianDong;

    @Column(name = "thoi_luong_lam_bai_phut")
    private Integer thoiLuongLamBaiPhut;

    @Column(name = "so_lan_lam_toi_da")
    private Integer soLanLamToiDa;

    @Enumerated(EnumType.STRING)
    @Column(name = "cach_tinh_diem")
    private CachTinhDiem cachTinhDiem;

    @Column(name = "cho_phep_xem_diem")
    private Boolean choPhepXemDiem;

    @Column(name = "cho_phep_xem_dap_an")
    private Boolean choPhepXemDapAn;

    @Column(name = "tron_cau_hoi")
    private Boolean tronCauHoi;

    @Column(name = "tron_dap_an")
    private Boolean tronDapAn;

    @Column(name = "cho_phep_lam_qua_han")
    private Boolean choPhepLamQuaHan;

    @Column(name = "giao_vien_duoc_chinh_sua")
    private Boolean giaoVienDuocChinhSua;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiBaiTapLop trangThai;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "diem_toi_da", nullable = false)
    private BigDecimal diemToiDa;

    @Column(name = "yeu_cau_toan_man_hinh")
    private Boolean yeuCauToanManHinh;

    @Column(name = "gioi_han_roi_tab")
    private Integer gioiHanRoiTab;

    @Column(name = "tu_dong_nop_khi_vi_pham")
    private Boolean tuDongNopKhiViPham;

    @Column(name = "mat_khau_lam_bai")
    private String matKhauLamBai;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
