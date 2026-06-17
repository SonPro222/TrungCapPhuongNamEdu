package org.example.trungcapphuongnam.module.daoTao.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "khung_ky_mau",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_khung_ky_mau_ma_ky",
                        columnNames = "ma_ky"
                ),
                @UniqueConstraint(
                        name = "uq_khung_ky_mau_thu_tu",
                        columnNames = "thu_tu"
                )
        }
)
public class KhungKyMau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_ky", nullable = false, length = 50)
    private String maKy;

    @Column(name = "ten_ky", nullable = false, length = 100)
    private String tenKy;

    @Column(name = "thu_tu", nullable = false)
    private Integer thuTu;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}