package org.example.trungcapphuongnam.module.daoTao.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "khung_ky",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_khung_ky_loai_ma", columnNames = {"loai_chuong_trinh_id", "ma_ky"}),
                @UniqueConstraint(name = "uk_khung_ky_loai_thu_tu", columnNames = {"loai_chuong_trinh_id", "thu_tu"})
        }
)
public class KhungKy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loai_chuong_trinh_id", nullable = false)
    private Long loaiChuongTrinhId;

    @Column(name = "ma_ky", nullable = false, length = 50)
    private String maKy;

    @Column(name = "ten_ky", nullable = false, length = 100)
    private String tenKy;

    @Column(name = "thu_tu", nullable = false)
    private Integer thuTu;
}
