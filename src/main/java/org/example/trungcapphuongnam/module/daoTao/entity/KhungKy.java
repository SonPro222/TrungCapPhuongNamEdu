package org.example.trungcapphuongnam.module.daoTao.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "khung_ky",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_khung_ky_version_ma",
                        columnNames = {"chuong_trinh_version_id", "ma_ky"}
                ),
                @UniqueConstraint(
                        name = "uk_khung_ky_version_thu_tu",
                        columnNames = {"chuong_trinh_version_id", "thu_tu"}
                )
        }
)
public class KhungKy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_version_id", nullable = false)
    private Long chuongTrinhVersionId;

    @Column(name = "ma_ky", nullable = false, length = 50)
    private String maKy;

    @Column(name = "ten_ky", nullable = false, length = 100)
    private String tenKy;

    @Column(name = "thu_tu", nullable = false)
    private Integer thuTu;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}