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
@Table(
        name = "tai_lieu_mau",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_tai_lieu_goc_ma", columnNames = "ma")
        }
)
public class TaiLieuGoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma", nullable = false, length = 100)
    private String ma;

    @Column(name = "ten", nullable = false, length = 500)
    private String ten;

    @Column(name = "loai", length = 100)
    private String loai;

    @Column(name = "tac_gia")
    private String tacGia;

    @Column(name = "nha_xuat_ban")
    private String nhaXuatBan;

    @Column(name = "nam_xuat_ban")
    private Integer namXuatBan;

    @Column(name = "duong_dan", columnDefinition = "TEXT")
    private String duongDan;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}