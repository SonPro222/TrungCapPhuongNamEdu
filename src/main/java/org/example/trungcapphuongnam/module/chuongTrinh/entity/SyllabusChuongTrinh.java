package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "syllabus_chuong_trinh")
public class SyllabusChuongTrinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;
    @Column(name = "syllabus_chuong_trinh_goc_id")
    private Long syllabusChuongTrinhGocId;
    @Column(name = "mo_ta_tong_quan")
    private String moTaTongQuan;

    @Column(name = "muc_dich")
    private String mucDich;

    @Column(name = "yeu_cau_dao_tao")
    private String yeuCauDaoTao;

    @Column(name = "phuong_phap_dao_tao")
    private String phuongPhapDaoTao;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "muc_tieu")
    private String mucTieu;

    @Column(name = "doi_tuong_tuyen_sinh")
    private String doiTuongTuyenSinh;

    @Column(name = "thoi_gian_dao_tao")
    private String thoiGianDaoTao;

    @Column(name = "khoi_luong_kien_thuc")
    private String khoiLuongKienThuc;

    @Column(name = "dieu_kien_tot_nghiep")
    private String dieuKienTotNghiep;

    @Column(name = "phuong_phap_danh_gia")
    private String phuongPhapDanhGia;

    @Column(name = "huong_dan_thuc_hien")
    private String huongDanThucHien;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
