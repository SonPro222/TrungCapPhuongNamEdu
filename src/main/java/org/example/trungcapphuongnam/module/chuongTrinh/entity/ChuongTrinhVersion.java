package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chuong_trinh_version")
public class ChuongTrinhVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_id")
    private Long chuongTrinhId;

    @Column(name = "ma_version")
    private String maVersion;

    @Column(name = "ten_version")
    private String tenVersion;

    @Column(name = "ngay_ap_dung")
    private LocalDate ngayApDung;

    @Column(name = "ngay_het_hieu_luc")
    private LocalDate ngayHetHieuLuc;

    @Column(name = "so_quyet_dinh")
    private String soQuyetDinh;

    @Column(name = "ngay_quyet_dinh")
    private LocalDate ngayQuyetDinh;

    @Column(name = "nguoi_ky")
    private String nguoiKy;

    @Column(name = "co_quan_ban_hanh")
    private String coQuanBanHanh;

    @Column(name = "file_quyet_dinh")
    private String fileQuyetDinh;

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

    @Column(name = "la_hien_hanh")
    private Boolean laHienHanh;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
