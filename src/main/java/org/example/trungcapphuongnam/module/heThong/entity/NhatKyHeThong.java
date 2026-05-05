package org.example.trungcapphuongnam.module.heThong.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nhat_ky_he_thong")
public class NhatKyHeThong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tai_khoan_id")
    private TaiKhoan taiKhoan;

    @Column(name = "hanh_dong", nullable = false, length = 100)
    private String hanhDong;

    @Column(name = "bang_tac_dong", length = 100)
    private String bangTacDong;

    @Column(name = "ban_ghi_id")
    private Long banGhiId;

    @Column(name = "noi_dung_cu", columnDefinition = "jsonb")
    private String noiDungCu;

    @Column(name = "noi_dung_moi", columnDefinition = "jsonb")
    private String noiDungMoi;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}