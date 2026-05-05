package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nhom_tu_chon")
public class NhomTuChon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;

    @Column(name = "ten")
    private String ten;

    @Column(name = "so_mon_chon")
    private Integer soMonChon;

    @Column(name = "so_tin_chi_can_dat")
    private BigDecimal soTinChiCanDat;

    @Column(name = "ghi_chu")
    private String ghiChu;

}
