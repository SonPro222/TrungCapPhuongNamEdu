package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lan_lam_bai_dap_an")
public class LanLamBaiDapAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lan_lam_bai_cau_hoi_id", nullable = false)
    private Long lanLamBaiCauHoiId;

    @Column(name = "dap_an_cau_hoi_version_id")
    private Long dapAnCauHoiVersionId;

    @Column(name = "noi_dung_snapshot", nullable = false, columnDefinition = "TEXT")
    private String noiDungSnapshot;

    @Column(name = "la_dap_an_dung_snapshot")
    private Boolean laDapAnDungSnapshot;

    @Column(name = "thu_tu", nullable = false)
    private Integer thuTu;
}
