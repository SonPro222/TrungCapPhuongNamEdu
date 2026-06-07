package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "lop_hoc_phan_chuong_trinh_mon",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_lhp_ctm",
                        columnNames = {"lop_hoc_phan_id", "chuong_trinh_mon_id"}
                )
        }
)
public class LopHocPhanChuongTrinhMon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lop_hoc_phan_id", nullable = false)
    private Long lopHocPhanId;

    @Column(name = "chuong_trinh_mon_id", nullable = false)
    private Long chuongTrinhMonId;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}