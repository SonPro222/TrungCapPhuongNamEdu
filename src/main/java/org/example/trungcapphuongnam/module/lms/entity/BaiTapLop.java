package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
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

    @Column(name = "bai_tap_online_id")
    private Long baiTapOnlineId;

    @Column(name = "lop_hoc_phan_id")
    private Long lopHocPhanId;

    @Column(name = "cau_hinh_danh_gia_id")
    private Long cauHinhDanhGiaId;

    @Column(name = "nguoi_giao_tai_khoan_id")
    private Long nguoiGiaoTaiKhoanId;

    @Column(name = "thoi_gian_mo")
    private LocalDateTime thoiGianMo;

    @Column(name = "thoi_gian_dong")
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

    @Column(name = "ghi_chu")
    private String ghiChu;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
