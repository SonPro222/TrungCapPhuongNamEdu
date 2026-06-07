package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiYeuCauDoiLich;

import java.time.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "yeu_cau_doi_lich")
public class YeuCauDoiLich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lich_hoc_cu_id", nullable = false)
    private Long lichHocCuId;

    @Column(name = "giao_vien_id", nullable = false)
    private Long giaoVienId;

    @Column(name = "ngay_moi")
    private LocalDate ngayMoi;

    @Column(name = "ca_hoc_moi_id")
    private Long caHocMoiId;

    @Column(name = "phong_hoc_moi_id")
    private Long phongHocMoiId;

    @Column(name = "ly_do", columnDefinition = "TEXT")
    private String lyDo;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiYeuCauDoiLich trangThai;

    @Column(name = "nguoi_duyet_id")
    private Long nguoiDuyetId;

    @Column(name = "ngay_duyet")
    private LocalDateTime ngayDuyet;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
