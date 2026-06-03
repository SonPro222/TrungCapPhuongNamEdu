package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "bai_nop_lms_tep")
public class BaiNopLmsTep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bai_nop_lms_id", nullable = false)
    private Long baiNopLmsId;

    @Column(name = "tep_dinh_kem_id", nullable = false)
    private Long tepDinhKemId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
