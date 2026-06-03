package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.MucDoCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiVersion;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cau_hoi_version")
public class CauHoiVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cau_hoi_goc_id", nullable = false)
    private Long cauHoiGocId;

    @Column(name = "version_no", nullable = false)
    private Integer versionNo;

    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_cau_hoi", nullable = false)
    private LoaiCauHoi loaiCauHoi;

    @Enumerated(EnumType.STRING)
    @Column(name = "muc_do")
    private MucDoCauHoi mucDo;

    @Column(name = "diem_mac_dinh")
    private BigDecimal diemMacDinh;

    @Column(name = "giai_thich_dap_an", columnDefinition = "TEXT")
    private String giaiThichDapAn;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiCauHoiVersion trangThai;

    @Column(name = "nang_luc_dau_ra_id")
    private Long nangLucDauRaId;

    @Column(name = "nguoi_duyet_tai_khoan_id")
    private Long nguoiDuyetTaiKhoanId;

    @Column(name = "thoi_gian_duyet")
    private LocalDateTime thoiGianDuyet;

    @Column(name = "noi_dung_hash")
    private String noiDungHash;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
