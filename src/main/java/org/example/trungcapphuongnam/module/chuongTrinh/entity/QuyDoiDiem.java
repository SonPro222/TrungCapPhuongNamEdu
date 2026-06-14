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
@Table(name = "quy_doi_diem")
public class QuyDoiDiem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "syllabus_mon_hoc_id", nullable = false)
    private Long syllabusMonHocId;

    @Column(name = "nguong_tu")
    private BigDecimal nguongTu;

    @Column(name = "nguong_den")
    private BigDecimal nguongDen;

    @Column(name = "diem_quy_doi")
    private BigDecimal diemQuyDoi;

    @Column(name = "ket_qua")
    private String ketQua;

    @Column(name = "cong_thuc")
    private String congThuc;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "loai_mau")
    private String loaiMau;



    @Column(name = "diem_toi_da")
    private BigDecimal diemToiDa;

    @Column(name = "thu_tu")
    private Integer thuTu;

    @Column(name = "bat_buoc")
    private Boolean batBuoc;
}
