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
@Table(name = "dap_an_cau_hoi")
public class DapAnCauHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cau_hoi_id")
    private Long cauHoiId;

    @Column(name = "noi_dung")
    private String noiDung;

    @Column(name = "la_dap_an_dung")
    private Boolean laDapAnDung;

    @Column(name = "thu_tu")
    private Integer thuTu;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
