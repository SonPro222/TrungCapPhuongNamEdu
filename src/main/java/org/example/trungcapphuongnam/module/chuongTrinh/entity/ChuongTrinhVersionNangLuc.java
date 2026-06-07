package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiNangLucDauRa;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiNangLucDauRaConverter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chuong_trinh_version_nang_luc")
public class ChuongTrinhVersionNangLuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "chuong_trinh_version_id", nullable = false)
    private Long chuongTrinhVersionId;
    @Column(name = "nang_luc_goc_id", nullable = false)
    private Long nangLucGocId;

    @Column(name = "ma", length = 50)
    private String ma;

    @Convert(converter = LoaiNangLucDauRaConverter.class)
    @Column(name = "loai", length = 50)
    private LoaiNangLucDauRa loai;

    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "thu_tu", nullable = true)
    private Integer thuTu;
    @Column(name = "ghi_chu", nullable = true, columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

