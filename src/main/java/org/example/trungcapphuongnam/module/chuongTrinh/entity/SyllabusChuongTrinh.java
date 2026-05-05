package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "syllabus_chuong_trinh")
public class SyllabusChuongTrinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;

    @Column(name = "mo_ta_tong_quan")
    private String moTaTongQuan;

    @Column(name = "muc_dich")
    private String mucDich;

    @Column(name = "yeu_cau_dao_tao")
    private String yeuCauDaoTao;

    @Column(name = "phuong_phap_dao_tao")
    private String phuongPhapDaoTao;

    @Column(name = "ghi_chu")
    private String ghiChu;

}
