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
@Table(
        name = "syllabus_chuong_trinh_goc",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_syllabus_chuong_trinh_goc_ma", columnNames = "ma")
        }
)
public class SyllabusChuongTrinhGoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma", nullable = false, length = 100)
    private String ma;

    @Column(name = "ten", nullable = false, length = 255)
    private String ten;

    @Column(name = "muc_tieu", columnDefinition = "TEXT")
    private String mucTieu;

    @Column(name = "doi_tuong_tuyen_sinh", columnDefinition = "TEXT")
    private String doiTuongTuyenSinh;

    @Column(name = "thoi_gian_dao_tao", columnDefinition = "TEXT")
    private String thoiGianDaoTao;

    @Column(name = "khoi_luong_kien_thuc", columnDefinition = "TEXT")
    private String khoiLuongKienThuc;

    @Column(name = "dieu_kien_tot_nghiep", columnDefinition = "TEXT")
    private String dieuKienTotNghiep;

    @Column(name = "phuong_phap_dao_tao", columnDefinition = "TEXT")
    private String phuongPhapDaoTao;

    @Column(name = "phuong_phap_danh_gia", columnDefinition = "TEXT")
    private String phuongPhapDanhGia;

    @Column(name = "huong_dan_thuc_hien", columnDefinition = "TEXT")
    private String huongDanThucHien;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}