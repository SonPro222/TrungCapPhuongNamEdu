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
@Table(
        name = "cau_hinh_ma_sinh_vien",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_cau_hinh_ma_sinh_vien",
                        columnNames = {"nganh_id", "chuong_trinh_version_id"}
                )
        }
)
public class CauHinhMaSinhVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nganh_id", nullable = false)
    private Long nganhId;

    @Column(name = "chuong_trinh_version_id", nullable = false)
    private Long chuongTrinhVersionId;

    @Column(name = "tien_to", nullable = false, length = 20)
    private String tienTo;

    @Column(name = "ma_dau", nullable = false, length = 20)
    private String maDau;

    @Column(name = "so_hien_tai", nullable = false)
    private Integer soHienTai;

    @Column(name = "do_dai_so_thu_tu", nullable = false)
    private Integer doDaiSoThuTu;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (tienTo == null || tienTo.isBlank()) {
            tienTo = "PN";
        }

        if (soHienTai == null) {
            soHienTai = 0;
        }

        if (doDaiSoThuTu == null) {
            doDaiSoThuTu = 3;
        }
    }
}