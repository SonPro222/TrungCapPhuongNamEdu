package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chuong_trinh_mon_quy_doi_diem_mau")
public class ChuongTrinhMonQuyDoiDiemMau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "chuong_trinh_mon_id", nullable = false)
    private Long chuongTrinhMonId;
    @Column(name = "quy_doi_diem_mau_id", nullable = false)
    private Long quyDoiDiemMauId;
    @Column(name = "ghi_chu", nullable = true, columnDefinition = "TEXT")
    private String ghiChu;
}
