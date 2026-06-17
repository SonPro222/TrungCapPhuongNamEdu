package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "giao_vien_dang_ky_nhom_ca")
public class GiaoVienDangKyNhomCa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "giao_vien_id", nullable = false)
    private Long giaoVienId;

    @Column(name = "khung_ky_id")
    private Long khungKyId;

    @Column(name = "thu_trong_tuan")
    private Integer thuTrongTuan;

    @Column(name = "ca_bat_dau_id", nullable = false)
    private Long caBatDauId;

    @Column(name = "ca_ket_thuc_id", nullable = false)
    private Long caKetThucId;

    @Column(name = "muc_do_uu_tien")
    private Integer mucDoUuTien;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
