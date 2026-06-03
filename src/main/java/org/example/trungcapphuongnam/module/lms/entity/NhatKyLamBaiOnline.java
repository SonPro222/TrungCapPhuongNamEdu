package org.example.trungcapphuongnam.module.lms.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nhat_ky_lam_bai_online")
public class NhatKyLamBaiOnline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lan_lam_bai_id", nullable = false)
    private Long lanLamBaiId;

    @Column(name = "hanh_dong", nullable = false)
    private String hanhDong;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "du_lieu")
    private JsonNode duLieu;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
