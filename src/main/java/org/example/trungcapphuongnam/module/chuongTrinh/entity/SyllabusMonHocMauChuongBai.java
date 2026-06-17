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
@Table(name = "syllabus_mon_hoc_mau_chuong_bai")
public class SyllabusMonHocMauChuongBai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "syllabus_mon_hoc_mau_id", nullable = false)
    private Long syllabusMonHocMauId;

    @Column(name = "ma_chuong", length = 100)
    private String maChuong;

    @Column(name = "ten", nullable = false, length = 255)
    private String ten;

    @Column(name = "muc_tieu", columnDefinition = "TEXT")
    private String mucTieu;

    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "thu_tu")
    private Integer thuTu;

    @Column(name = "tong_gio", precision = 6, scale = 2)
    private BigDecimal tongGio;

    @Column(name = "gio_ly_thuyet", precision = 6, scale = 2)
    private BigDecimal gioLyThuyet;

    @Column(name = "gio_thuc_hanh", precision = 6, scale = 2)
    private BigDecimal gioThucHanh;

    @Column(name = "gio_kiem_tra", precision = 6, scale = 2)
    private BigDecimal gioKiemTra;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}