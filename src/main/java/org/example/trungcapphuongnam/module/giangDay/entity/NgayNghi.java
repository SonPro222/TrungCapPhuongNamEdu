package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiNgayNghi;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ngay_nghi")
public class NgayNghi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Giữ lại cột ngay để tương thích dữ liệu cũ.
     * Với khoảng nghỉ nhiều ngày, ngay thường bằng ngayBatDau.
     */
    @Column(name = "ngay", nullable = false)
    private LocalDate ngay;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Column(name = "ten_ngay_nghi")
    private String tenNgayNghi;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_ngay_nghi")
    private LoaiNgayNghi loaiNgayNghi;

    @Column(name = "lap_lai_hang_nam")
    private Boolean lapLaiHangNam;

    @Column(name = "tinh_la_ngay_khong_hoc")
    private Boolean tinhLaNgayKhongHoc;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    /**
     * Null = áp dụng chung. Có giá trị = chỉ áp dụng cho chương trình version đó.
     */
    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;

    /**
     * Null = áp dụng chung. Có giá trị = chỉ áp dụng riêng kỳ đó.
     */
    @Column(name = "khung_ky_id")
    private Long khungKyId;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void chuanHoaKhoangNgay() {
        if (ngayBatDau == null) {
            ngayBatDau = ngay;
        }
        if (ngayKetThuc == null) {
            ngayKetThuc = ngayBatDau;
        }
        if (ngay == null) {
            ngay = ngayBatDau;
        }
        if (ngayBatDau != null && ngayKetThuc != null && ngayKetThuc.isBefore(ngayBatDau)) {
            LocalDate tmp = ngayBatDau;
            ngayBatDau = ngayKetThuc;
            ngayKetThuc = tmp;
            ngay = ngayBatDau;
        }
        if (loaiNgayNghi == null) {
            loaiNgayNghi = LoaiNgayNghi.KHAC;
        }
        if (lapLaiHangNam == null) {
            lapLaiHangNam = false;
        }
        if (tinhLaNgayKhongHoc == null) {
            tinhLaNgayKhongHoc = true;
        }
        if (trangThai == null) {
            trangThai = true;
        }
    }
}
