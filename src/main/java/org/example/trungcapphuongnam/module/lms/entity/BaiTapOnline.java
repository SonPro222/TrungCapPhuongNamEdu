package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.LoaiBaiOnline;
import org.example.trungcapphuongnam.module.lms.enums.NguonTao;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiBaiTapOnline;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bai_tap_online")
public class BaiTapOnline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_mon_id", nullable = false)
    private Long chuongTrinhMonId;

    @Column(name = "nguoi_tao_tai_khoan_id")
    private Long nguoiTaoTaiKhoanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "nguon_tao")
    private NguonTao nguonTao;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_bai")
    private LoaiBaiOnline loaiBai;

    @Column(name = "tong_diem")
    private BigDecimal tongDiem;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiBaiTapOnline trangThai;

    @Column(name = "tron_cau_hoi")
    private Boolean tronCauHoi;

    @Column(name = "tron_dap_an")
    private Boolean tronDapAn;

    @Column(name = "sinh_de_tu_dong")
    private Boolean sinhDeTuDong;

    @Column(name = "hien_dap_an_sau_khi_nop")
    private Boolean hienDapAnSauKhiNop;

    @Column(name = "hien_giai_thich_sau_khi_nop")
    private Boolean hienGiaiThichSauKhiNop;

    @Column(name = "ma_tran_de_id")
    private Long maTranDeId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
