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
@Table(name = "quy_doi_diem")
public class QuyDoiDiem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_mon_id")
    private Long chuongTrinhMonId;

    @Column(name = "nguong_tu")
    private BigDecimal nguongTu;

    @Column(name = "nguong_den")
    private BigDecimal nguongDen;

    @Column(name = "diem_quy_doi")
    private BigDecimal diemQuyDoi;

    @Column(name = "ket_qua")
    private String ketQua;

    @Column(name = "cong_thuc")
    private String congThuc;

    @Column(name = "ghi_chu")
    private String ghiChu;

}
