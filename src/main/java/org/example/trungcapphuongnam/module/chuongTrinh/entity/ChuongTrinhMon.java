package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chuong_trinh_mon")
public class ChuongTrinhMon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;

    @Column(name = "mon_hoc_id")
    private Long monHocId;

    @Column(name = "ma_mon_trong_ct")
    private String maMonTrongCt;

    @Column(name = "khung_ky_id")
    private Long khungKyId;

    @Column(name = "nhom_kien_thuc_id")
    private Long nhomKienThucId;

    @Column(name = "loai")
    private String loai;

    @Column(name = "loai_hoc_phan")
    private String loaiHocPhan;

    @Column(name = "bat_buoc")
    private Boolean batBuoc;

    @Column(name = "la_mon_dieu_kien")
    private Boolean laMonDieuKien;

    @Column(name = "thu_tu")
    private Integer thuTu;

    @Column(name = "so_tin_chi")
    private BigDecimal soTinChi;

    @Column(name = "tong_gio")
    private BigDecimal tongGio;

    @Column(name = "gio_ly_thuyet")
    private BigDecimal gioLyThuyet;

    @Column(name = "gio_thuc_hanh")
    private BigDecimal gioThucHanh;

    @Column(name = "gio_kiem_tra")
    private BigDecimal gioKiemTra;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
