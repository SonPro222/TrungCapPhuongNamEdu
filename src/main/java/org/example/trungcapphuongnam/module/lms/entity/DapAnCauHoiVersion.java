package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dap_an_cau_hoi_version")
public class DapAnCauHoiVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cau_hoi_version_id", nullable = false)
    private Long cauHoiVersionId;

    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "la_dap_an_dung")
    private Boolean laDapAnDung;

    @Column(name = "thu_tu", nullable = false)
    private Integer thuTu;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
