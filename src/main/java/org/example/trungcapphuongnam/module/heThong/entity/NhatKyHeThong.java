package org.example.trungcapphuongnam.module.heThong.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nhat_ky_he_thong")
public class NhatKyHeThong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tai_khoan_id")
    private TaiKhoan taiKhoan;

    @Column(name = "hanh_dong", nullable = false, length = 100)
    private String hanhDong;

    @Column(name = "bang_tac_dong", length = 100)
    private String bangTacDong;

    @Column(name = "ban_ghi_id")
    private Long banGhiId;

    @Column(name = "ban_ghi_ma", length = 100)
    private String banGhiMa;

    @Column(name = "ban_ghi_ten", length = 255)
    private String banGhiTen;

    @Column(name = "noi_dung_cu", columnDefinition = "TEXT")
    private String noiDungCu;

    @Column(name = "noi_dung_moi", columnDefinition = "TEXT")
    private String noiDungMoi;

    @Column(name = "du_lieu_tim_kiem", columnDefinition = "TEXT")
    private String duLieuTimKiem;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "tai_khoan_email", length = 255)
    private String taiKhoanEmail;

    @Column(name = "ten_tai_khoan", length = 255)
    private String tenTaiKhoan;

    @Column(name = "loai_tai_khoan", length = 50)
    private String loaiTaiKhoan;

    @Column(name = "doi_tuong_loai", length = 50)
    private String doiTuongLoai;

    @Column(name = "doi_tuong_id")
    private Long doiTuongId;

    @Column(name = "doi_tuong_ma", length = 100)
    private String doiTuongMa;

    @Column(name = "doi_tuong_ten", length = 255)
    private String doiTuongTen;

    @Column(name = "doi_tuong_email", length = 255)
    private String doiTuongEmail;

    @Column(name = "module_nghiep_vu", length = 100)
    private String moduleNghiepVu;

    @Column(name = "chuc_nang", length = 100)
    private String chucNang;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "phuong_thuc", length = 20)
    private String phuongThuc;

    @Column(name = "duong_dan", columnDefinition = "TEXT")
    private String duongDan;

    @Column(name = "ket_qua", length = 50)
    private String ketQua;
}