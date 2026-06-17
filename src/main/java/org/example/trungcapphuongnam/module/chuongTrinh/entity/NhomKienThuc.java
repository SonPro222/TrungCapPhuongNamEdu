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
@Table(name = "nhom_kien_thuc")
public class NhomKienThuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;
    @Column(name = "nhom_kien_thuc_mau_id")
    private Long nhomKienThucMauId;
    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "thu_tu")
    private Integer thuTu;

    @Column(name = "loai_nhom")
    private String loaiNhom;

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

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
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
