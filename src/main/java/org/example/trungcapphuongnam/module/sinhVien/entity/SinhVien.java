package org.example.trungcapphuongnam.module.sinhVien.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.sinhVien.enums.GioiTinh;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienTrangThai;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sinh_vien")
public class SinhVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_sinh_vien", nullable = false, unique = true, length = 50)
    private String maSinhVien;

    @Column(name = "ho_ten", nullable = false, length = 255)
    private String hoTen;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Enumerated(EnumType.STRING)
    @Column(name = "gioi_tinh", length = 20)
    private GioiTinh gioiTinh;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "so_dien_thoai", length = 50)
    private String soDienThoai;

    @Column(name = "dia_chi", columnDefinition = "TEXT")
    private String diaChi;

    @Column(name = "dia_chi_thuong_tru", columnDefinition = "TEXT")
    private String diaChiThuongTru;

    @Column(name = "so_cccd", length = 50)
    private String soCccd;

    @Column(name = "ngay_cap_cccd")
    private LocalDate ngayCapCccd;

    @Column(name = "noi_cap_cccd", length = 255)
    private String noiCapCccd;

    @Column(name = "anh_chan_dung_tep_id")
    private Long anhChanDungTepId;

    @Column(name = "cccd_truoc_tep_id")
    private Long cccdTruocTepId;

    @Column(name = "cccd_sau_tep_id")
    private Long cccdSauTepId;

    @Column(name = "bang_cap_tep_id")
    private Long bangCapTepId;

    @Column(name = "bang_cap", length = 255)
    private String bangCap;

    @Column(name = "nam_tot_nghiep")
    private Integer namTotNghiep;

    @Column(name = "truong_tot_nghiep", length = 255)
    private String truongTotNghiep;

    @Column(name = "ho_ten_cha", length = 255)
    private String hoTenCha;

    @Column(name = "nghe_nghiep_cha", length = 255)
    private String ngheNghiepCha;

    @Column(name = "sdt_cha", length = 50)
    private String sdtCha;

    @Column(name = "email_cha", length = 255)
    private String emailCha;

    @Column(name = "ho_ten_me", length = 255)
    private String hoTenMe;

    @Column(name = "nghe_nghiep_me", length = 255)
    private String ngheNghiepMe;

    @Column(name = "sdt_me", length = 50)
    private String sdtMe;

    @Column(name = "email_me", length = 255)
    private String emailMe;

    @Column(name = "ho_ten_nguoi_than", length = 255)
    private String hoTenNguoiThan;

    @Column(name = "quan_he_nguoi_than", length = 100)
    private String quanHeNguoiThan;

    @Column(name = "sdt_nguoi_than", length = 50)
    private String sdtNguoiThan;

    @Column(name = "email_nguoi_than", length = 255)
    private String emailNguoiThan;

    @Column(name = "ghi_chu_ho_so", columnDefinition = "TEXT")
    private String ghiChuHoSo;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private SinhVienTrangThai trangThai;

    @Column(name = "tai_khoan_id", unique = true)
    private Long taiKhoanId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (trangThai == null) {
            trangThai = SinhVienTrangThai.dang_hoc;
        }
    }
}
