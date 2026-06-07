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
@Table(name = "syllabus_mon_hoc")
public class SyllabusMonHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_mon_id")
    private Long chuongTrinhMonId;
    @Column(name = "so_tin_chi", precision = 6, scale = 1)
    private BigDecimal soTinChi;
    @Column(name = "syllabus_mon_hoc_goc_id")
    private Long syllabusMonHocGocId;

    @Column(name = "vi_tri")
    private String viTri;

    @Column(name = "tinh_chat")
    private String tinhChat;
    @Column(name = "so_buoi_hoc", nullable = false)
    private Integer soBuoiHoc;

    @Column(name = "so_tiet_moi_buoi", nullable = false, precision = 4, scale = 1)
    private BigDecimal soTietMoiBuoi;

    @Column(name = "so_phut_mot_tiet", nullable = false)
    private Integer soPhutMotTiet;
    @Column(name = "muc_tieu")
    private String mucTieu;

    @Column(name = "phuong_phap_danh_gia")
    private String phuongPhapDanhGia;

    @Column(name = "dieu_kien_hoan_thanh")
    private String dieuKienHoanThanh;

    @Column(name = "huong_dan")
    private String huongDan;

    @Column(name = "diem_dat_toi_thieu", precision = 5, scale = 2)
    private BigDecimal diemDatToiThieu;

    @Column(name = "don_vi_diem")
    private String donViDiem;

    @Column(name = "ty_le_chuyen_can_toi_thieu")
    private BigDecimal tyLeChuyenCanToiThieu;

    @Column(name = "bat_buoc_du_thi")
    private Boolean batBuocDuThi;

    @Column(name = "cong_thuc_quy_doi")
    private String congThucQuyDoi;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "mon_hoc_id")
    private Long monHocId;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "ghi_chu")
    private String ghiChu;
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (this.createdAt == null) {
            this.createdAt = now;
        }

        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
