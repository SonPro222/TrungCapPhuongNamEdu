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
@Table(name = "v_syllabus_mon_hoc_tong_hop")
public class SyllabusMonHocTongHopView {

    @Id
    @Column(name = "syllabus_mon_hoc_id")
    private Long syllabusMonHocId;

    @Column(name = "chuong_trinh_mon_id")
    private Long chuongTrinhMonId;

    @Column(name = "mon_hoc_id")
    private Long monHocId;

    @Column(name = "so_tin_chi")
    private BigDecimal soTinChi;

    @Column(name = "so_buoi_hoc")
    private Integer soBuoiHoc;

    @Column(name = "so_tiet_moi_buoi")
    private BigDecimal soTietMoiBuoi;

    @Column(name = "so_phut_mot_tiet")
    private Integer soPhutMotTiet;

    @Column(name = "tong_gio")
    private BigDecimal tongGio;

    @Column(name = "gio_ly_thuyet")
    private BigDecimal gioLyThuyet;

    @Column(name = "gio_thuc_hanh")
    private BigDecimal gioThucHanh;

    @Column(name = "gio_kiem_tra")
    private BigDecimal gioKiemTra;
}