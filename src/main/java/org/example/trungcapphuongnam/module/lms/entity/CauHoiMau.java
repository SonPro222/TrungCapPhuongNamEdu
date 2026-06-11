package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiGoc;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cau_hoi_mau")
public class CauHoiGoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mon_hoc_id", nullable = false)
    private Long monHocId;

    @Column(name = "ma_cau_hoi")
    private String maCauHoi;

    @Column(name = "nguoi_tao_tai_khoan_id")
    private Long nguoiTaoTaiKhoanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiCauHoiGoc trangThai;

    @Column(name = "don_vi_so_huu_id")
    private Long donViSoHuuId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
