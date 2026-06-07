package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.VaiTroGiangDay;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "phan_cong_giang_day")
public class PhanCongGiangDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lop_hoc_phan_id", nullable = false)
    private Long lopHocPhanId;

    @Column(name = "giao_vien_id", nullable = false)
    private Long giaoVienId;

    @Enumerated(EnumType.STRING)
    @Column(name = "vai_tro")
    private VaiTroGiangDay vaiTro;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

}
