package org.example.trungcapphuongnam.module.heThong.entity;


import jakarta.persistence.*;
        import lombok.*;
import org.example.trungcapphuongnam.module.heThong.constant.LoaiNguoiGui;
import org.example.trungcapphuongnam.module.heThong.constant.LoaiNoiDungTep;
import org.example.trungcapphuongnam.module.heThong.constant.TrangThaiTepDinhKem;

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