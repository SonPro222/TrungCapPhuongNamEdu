package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chuong_trinh_version_muc_tieu")
public class ChuongTrinhVersionMucTieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "chuong_trinh_version_id", nullable = false)
    private Long chuongTrinhVersionId;
    @Column(name = "muc_tieu_mau_id", nullable = false)
    private Long mucTieuMauId;
    @Column(name = "thu_tu", nullable = true)
    private Integer thuTu;
    @Column(name = "ghi_chu", nullable = true, columnDefinition = "TEXT")
    private String ghiChu;
}
