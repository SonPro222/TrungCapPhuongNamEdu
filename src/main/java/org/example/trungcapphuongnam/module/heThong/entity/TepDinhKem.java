package org.example.trungcapphuongnam.module.heThong.entity;


import jakarta.persistence.*;
        import lombok.*;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiNguoiGui;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiNoiDungTep;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiTepDinhKem;

import java.time.LocalDateTime;

@Entity
@Table(name = "tep_dinh_kem")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TepDinhKem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_goc", nullable = false, length = 500)
    private String tenGoc;

    @Column(name = "ten_luu", nullable = false, length = 500)
    private String tenLuu;

    @Column(name = "duong_dan_tuong_doi", nullable = false, length = 1000)
    private String duongDanTuongDoi;

    @Column(name = "duong_dan_day_du", length = 1500)
    private String duongDanDayDu;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_noi_dung", nullable = false, length = 100)
    private LoaiNoiDungTep loaiNoiDung;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "extension", length = 50)
    private String extension;

    @Column(name = "dung_luong", nullable = false)
    private Long dungLuong;

    @Column(name = "module", length = 100)
    private String module;

    @Column(name = "nghiep_vu", length = 100)
    private String nghiepVu;

    @Column(name = "doi_tuong_id")
    private Long doiTuongId;

    @Enumerated(EnumType.STRING)
    @Column(name = "nguoi_gui_loai", nullable = false, length = 50)
    private LoaiNguoiGui nguoiGuiLoai;

    @Column(name = "nguoi_gui_id")
    private Long nguoiGuiId;

    @Column(name = "nguoi_gui_ten")
    private String nguoiGuiTen;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;
    @Column(name = "nganh_id")
    private Long nganhId;

    @Column(name = "chuong_trinh_id")
    private Long chuongTrinhId;

    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;

    @Column(name = "nam_hoc_id")
    private Long namHocId;

    @Column(name = "hoc_ky_id")
    private Long hocKyId;

    @Column(name = "lop_hanh_chinh_id")
    private Long lopHanhChinhId;

    @Column(name = "lop_hoc_phan_id")
    private Long lopHocPhanId;

    @Column(name = "mon_hoc_id")
    private Long monHocId;

    @Column(name = "chuong_trinh_mon_id")
    private Long chuongTrinhMonId;

    @Column(name = "giang_vien_id")
    private Long giangVienId;

    @Column(name = "sinh_vien_id")
    private Long sinhVienId;

    @Column(name = "bai_hoc_id")
    private Long baiHocId;

    @Column(name = "bai_tap_id")
    private Long baiTapId;

    @Column(name = "lab_id")
    private Long labId;

    @Column(name = "bai_nop_id")
    private Long baiNopId;

    @Column(name = "lan_nop")
    private Integer lanNop;

    @Column(name = "syllabus_chuong_trinh_id")
    private Long syllabusChuongTrinhId;

    @Column(name = "syllabus_mon_hoc_id")
    private Long syllabusMonHocId;

    @Column(name = "tai_lieu_goc_id")
    private Long taiLieuGocId;

    @Column(name = "tang_nghiep_vu", length = 150)
    private String tangNghiepVu;

    @Column(name = "duong_dan_nghiep_vu", length = 1500)
    private String duongDanNghiepVu;
    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 30)
    private TrangThaiTepDinhKem trangThai;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (ngayTao == null) {
            ngayTao = now;
        }

        ngayCapNhat = now;

        if (trangThai == null) {
            trangThai = TrangThaiTepDinhKem.DANG_SU_DUNG;
        }
    }

    @PreUpdate
    public void preUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}