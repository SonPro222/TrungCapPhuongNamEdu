package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.BigDecimal;


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

}
