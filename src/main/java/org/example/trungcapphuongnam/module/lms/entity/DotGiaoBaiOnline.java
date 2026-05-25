package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.NguonGiao;
import org.example.trungcapphuongnam.module.lms.enums.PhamViGiaoBai;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dot_giao_bai_online")
public class DotGiaoBaiOnline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bai_tap_online_id")
    private Long baiTapOnlineId;

    @Column(name = "nguoi_giao_tai_khoan_id")
    private Long nguoiGiaoTaiKhoanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "nguon_giao")
    private NguonGiao nguonGiao;

    @Enumerated(EnumType.STRING)
    @Column(name = "pham_vi")
    private PhamViGiaoBai phamVi;

    @Column(name = "nganh_id")
    private Long nganhId;

    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
