package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.MucDoCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.NguonTao;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiSuDung;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ngan_hang_cau_hoi_legacy")
public class NganHangCauHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_mon_id")
    private Long chuongTrinhMonId;

    @Column(name = "nguoi_tao_tai_khoan_id")
    private Long nguoiTaoTaiKhoanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "nguon_tao")
    private NguonTao nguonTao;

    @Column(name = "noi_dung")
    private String noiDung;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_cau_hoi")
    private LoaiCauHoi loaiCauHoi;

    @Enumerated(EnumType.STRING)
    @Column(name = "muc_do")
    private MucDoCauHoi mucDo;

    @Column(name = "diem_mac_dinh")
    private BigDecimal diemMacDinh;

    @Column(name = "giai_thich_dap_an")
    private String giaiThichDapAn;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiSuDung trangThai;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
