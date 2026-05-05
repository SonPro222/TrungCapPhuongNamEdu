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
        name = "tai_khoan_vai_tro",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_tai_khoan_vai_tro",
                        columnNames = {"tai_khoan_id", "vai_tro_id"}
                )
        }
)
public class TaiKhoanVaiTro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tai_khoan_id", nullable = false)
    private TaiKhoan taiKhoan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vai_tro_id", nullable = false)
    private VaiTro vaiTro;
}