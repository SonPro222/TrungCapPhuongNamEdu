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
@Table(
        name = "syllabus_mon_hoc_mau",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_smhg_ma", columnNames = "ma"),
                @UniqueConstraint(name = "uq_smhg_mon_hoc_ten", columnNames = {"mon_hoc_id", "ten"})
        }
)
public class SyllabusMonHocMau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mon_hoc_id", nullable = false)
    private Long monHocId;

    @Column(name = "ma", nullable = false, length = 100)
    private String ma;
    @Column(name = "so_tin_chi", precision = 6, scale = 1)
    private BigDecimal soTinChi;
    @Column(name = "ten", nullable = false, length = 255)
    private String ten;

    @Column(name = "vi_tri", columnDefinition = "TEXT")
    private String viTri;
    @Column(name = "so_buoi_hoc", nullable = false)
    private Integer soBuoiHoc;

    @Column(name = "so_tiet_moi_buoi", nullable = false, precision = 4, scale = 1)
    private BigDecimal soTietMoiBuoi;

    @Column(name = "so_phut_mot_tiet", nullable = false)
    private Integer soPhutMotTiet;
    @Column(name = "tinh_chat", columnDefinition = "TEXT")
    private String tinhChat;

    @Column(name = "muc_tieu", columnDefinition = "TEXT")
    private String mucTieu;

    @Column(name = "phuong_phap_danh_gia", columnDefinition = "TEXT")
    private String phuongPhapDanhGia;

    @Column(name = "dieu_kien_hoan_thanh", columnDefinition = "TEXT")
    private String dieuKienHoanThanh;

    @Column(name = "huong_dan", columnDefinition = "TEXT")
    private String huongDan;

    @Column(name = "diem_dat_toi_thieu", precision = 5, scale = 2)
    private BigDecimal diemDatToiThieu;

    @Column(name = "don_vi_diem", length = 20)
    private String donViDiem;

    @Column(name = "ty_le_chuyen_can_toi_thieu", precision = 5, scale = 2)
    private BigDecimal tyLeChuyenCanToiThieu;

    @Column(name = "bat_buoc_du_thi")
    private Boolean batBuocDuThi;

    @Column(name = "cong_thuc_quy_doi", columnDefinition = "TEXT")
    private String congThucQuyDoi;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}