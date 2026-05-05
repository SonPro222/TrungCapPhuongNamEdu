package org.example.trungcapphuongnam.module.heThong.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "vai_tro_quyen",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_vai_tro_quyen",
                        columnNames = {"vai_tro_id", "quyen_id"}
                )
        }
)
public class VaiTroQuyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vai_tro_id", nullable = false)
    private VaiTro vaiTro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quyen_id", nullable = false)
    private Quyen quyen;
}