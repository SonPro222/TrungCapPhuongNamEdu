package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vi_tri_viec_lam_mau")
public class ViTriViecLamGoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_id", nullable = false)
    private Long chuongTrinhId;

    @Column(name = "ma", nullable = true, length = 50)
    private String ma;
    @Column(name = "ten", nullable = false, length = 255)
    private String ten;
    @Column(name = "mo_ta", nullable = true, columnDefinition = "TEXT")
    private String moTa;
    @Column(name = "ghi_chu", nullable = true, columnDefinition = "TEXT")
    private String ghiChu;
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
