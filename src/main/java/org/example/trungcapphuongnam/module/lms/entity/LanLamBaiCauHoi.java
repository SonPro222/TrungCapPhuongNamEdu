package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lan_lam_bai_cau_hoi")
public class LanLamBaiCauHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lan_lam_bai_id", nullable = false)
    private Long lanLamBaiId;

    @Column(name = "cau_hoi_version_id", nullable = false)
    private Long cauHoiVersionId;

    @Column(name = "noi_dung_snapshot", nullable = false, columnDefinition = "TEXT")
    private String noiDungSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_cau_hoi_snapshot")
    private LoaiCauHoi loaiCauHoiSnapshot;

    @Column(name = "diem_snapshot")
    private BigDecimal diemSnapshot;

    @Column(name = "thu_tu", nullable = false)
    private Integer thuTu;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
