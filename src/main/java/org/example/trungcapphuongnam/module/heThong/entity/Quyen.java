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
        name = "quyen",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_quyen_ma_quyen", columnNames = "ma_quyen")
        }
)
public class Quyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_quyen", nullable = false, unique = true, length = 100)
    private String maQuyen;

    @Column(name = "ten_quyen", nullable = false, length = 255)
    private String tenQuyen;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @OneToMany(mappedBy = "quyen", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<VaiTroQuyen> vaiTroQuyens = new LinkedHashSet<>();
}