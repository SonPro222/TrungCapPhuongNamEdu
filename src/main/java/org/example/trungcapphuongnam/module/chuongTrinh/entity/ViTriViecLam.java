package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vi_tri_viec_lam")
public class ViTriViecLam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;

    @Column(name = "ten")
    private String ten;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "thu_tu")
    private Integer thuTu;

}
