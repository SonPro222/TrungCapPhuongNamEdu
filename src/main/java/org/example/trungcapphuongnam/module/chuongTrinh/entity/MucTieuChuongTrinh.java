package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "muc_tieu_chuong_trinh")
public class MucTieuChuongTrinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;

    @Column(name = "loai")
    private String loai;

    @Column(name = "noi_dung")
    private String noiDung;

    @Column(name = "thu_tu")
    private Integer thuTu;

}
