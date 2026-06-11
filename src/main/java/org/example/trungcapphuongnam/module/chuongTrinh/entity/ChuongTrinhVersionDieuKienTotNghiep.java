package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chuong_trinh_version_dieu_kien_tot_nghiep")
public class ChuongTrinhVersionDieuKienTotNghiep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "chuong_trinh_version_id", nullable = false)
    private Long chuongTrinhVersionId;
    @Column(name = "dieu_kien_mau_id", nullable = false)
    private Long dieuKienMauId;
    @Column(name = "thu_tu", nullable = true)
    private Integer thuTu;
    @Column(name = "ghi_chu", nullable = true, columnDefinition = "TEXT")
    private String ghiChu;
}
