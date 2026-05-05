package org.example.trungcapphuongnam.module.daoTao.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "loai_chuong_trinh")
public class LoaiChuongTrinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_loai", nullable = false, unique = true, length = 50)
    private String maLoai;

    @Column(name = "ten_loai", nullable = false, length = 100)
    private String tenLoai;

    @Column(name = "so_thang", nullable = false)
    private Integer soThang;

    @Column(name = "so_ky", nullable = false)
    private Integer soKy;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;
}
