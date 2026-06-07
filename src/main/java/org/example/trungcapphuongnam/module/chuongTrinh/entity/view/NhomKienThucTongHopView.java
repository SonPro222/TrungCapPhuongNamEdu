package org.example.trungcapphuongnam.module.chuongTrinh.entity.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;

@Getter
@Entity
@Immutable
@Table(name = "v_nhom_kien_thuc_tong_hop")
public class NhomKienThucTongHopView {

    @Id
    @Column(name = "nhom_kien_thuc_id")
    private Long nhomKienThucId;

    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;

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