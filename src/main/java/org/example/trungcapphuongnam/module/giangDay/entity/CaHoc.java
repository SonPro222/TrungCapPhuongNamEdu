package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiCaHoc;

import java.time.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ca_hoc")
public class CaHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_ca", nullable = false)
    private String maCa;

    @Column(name = "ten_ca", nullable = false)
    private String tenCa;

    @Column(name = "gio_bat_dau", nullable = false)
    private LocalTime gioBatDau;

    @Column(name = "gio_ket_thuc", nullable = false)
    private LocalTime gioKetThuc;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "thu_tu")
    private Integer thuTu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiCaHoc trangThai;
}
