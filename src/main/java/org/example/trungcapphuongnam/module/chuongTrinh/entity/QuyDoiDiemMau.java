package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.KetQuaDatKhongDat;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.KetQuaDatKhongDatConverter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quy_doi_diem_mau")
public class QuyDoiDiemMau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "syllabus_mon_hoc_mau_id")
    private Long syllabusMonHocMauId;

    @Column(name = "ma", nullable = true, length = 50)
    private String ma;

    @Column(name = "ten", nullable = false, length = 255)
    private String ten;

    @Column(name = "nguong_tu", nullable = true)
    private BigDecimal nguongTu;

    @Column(name = "nguong_den", nullable = true)
    private BigDecimal nguongDen;

    @Column(name = "diem_quy_doi", nullable = true)
    private BigDecimal diemQuyDoi;

    @Convert(converter = KetQuaDatKhongDatConverter.class)
    @Column(name = "ket_qua", nullable = true, length = 50)
    private KetQuaDatKhongDat ketQua;

    @Column(name = "cong_thuc", nullable = true, columnDefinition = "TEXT")
    private String congThuc;

    @Column(name = "ghi_chu", nullable = true, columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "loai_mau", nullable = true, length = 50)
    private String loaiMau;

    @Column(name = "ty_le", nullable = true)
    private BigDecimal tyLe;

    @Column(name = "diem_toi_da", nullable = true)
    private BigDecimal diemToiDa;

    @Column(name = "thu_tu", nullable = true)
    private Integer thuTu;

    @Column(name = "bat_buoc", nullable = true)
    private Boolean batBuoc;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
