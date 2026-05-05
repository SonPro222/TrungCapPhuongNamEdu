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
@Table(name = "nhom_kien_thuc")
public class NhomKienThuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "thu_tu")
    private Integer thuTu;

    @Column(name = "loai_nhom")
    private String loaiNhom;

    @Column(name = "tong_tin_chi")
    private BigDecimal tongTinChi;

    @Column(name = "tong_so_gio")
    private BigDecimal tongSoGio;

    @Column(name = "tong_gio_ly_thuyet")
    private BigDecimal tongGioLyThuyet;

    @Column(name = "tong_gio_thuc_hanh")
    private BigDecimal tongGioThucHanh;

    @Column(name = "tong_gio_kiem_tra")
    private BigDecimal tongGioKiemTra;

}
