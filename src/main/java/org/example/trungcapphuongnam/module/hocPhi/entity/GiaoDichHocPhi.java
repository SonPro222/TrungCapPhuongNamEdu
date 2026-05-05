package org.example.trungcapphuongnam.module.hocPhi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.PhuongThucGiaoDichHocPhi;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "giao_dich_hoc_phi")
public class GiaoDichHocPhi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hoc_phi_id", nullable = false)
    private Long hocPhiId;

    @Column(name = "so_tien", nullable = false, precision = 14, scale = 2)
    private BigDecimal soTien;

    @Column(name = "ngay_giao_dich")
    private LocalDateTime ngayGiaoDich;

    @Enumerated(EnumType.STRING)
    @Column(name = "phuong_thuc", length = 50)
    private PhuongThucGiaoDichHocPhi phuongThuc;

    @Column(name = "ma_giao_dich", length = 100)
    private String maGiaoDich;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.ngayGiaoDich == null) {
            this.ngayGiaoDich = now;
        }
        this.createdAt = now;
    }
}
