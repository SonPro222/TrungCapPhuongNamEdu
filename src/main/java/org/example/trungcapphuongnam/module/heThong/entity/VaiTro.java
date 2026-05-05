package org.example.trungcapphuongnam.module.heThong.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "vai_tro",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_vai_tro_ma_vai_tro", columnNames = "ma_vai_tro")
        }
)
public class VaiTro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_vai_tro", nullable = false, unique = true, length = 50)
    private String maVaiTro;

    @Column(name = "ten_vai_tro", nullable = false, length = 100)
    private String tenVaiTro;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @OneToMany(mappedBy = "vaiTro", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<TaiKhoanVaiTro> taiKhoanVaiTros = new LinkedHashSet<>();

    @OneToMany(mappedBy = "vaiTro", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<VaiTroQuyen> vaiTroQuyens = new LinkedHashSet<>();
}