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
@Table(name = "cau_tra_loi_chon_dap_an")
public class CauTraLoiChonDapAn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cau_tra_loi_id", nullable = false)
    private Long cauTraLoiId;

    @Column(name = "lan_lam_bai_dap_an_id", nullable = false)
    private Long lanLamBaiDapAnId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
