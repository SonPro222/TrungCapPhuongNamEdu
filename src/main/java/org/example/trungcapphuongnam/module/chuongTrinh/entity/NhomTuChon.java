package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "nhom_tu_chon")
public class NhomTuChon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chuong_trinh_version_id")
    private Long chuongTrinhVersionId;
    @Column(name = "nhom_tu_chon_mau_id")
    private Long nhomTuChonMauId;
    @Column(name = "ten")
    private String ten;

    @Column(name = "so_mon_chon")
    private Integer soMonChon;

    @Column(name = "so_tin_chi_can_dat")
    private BigDecimal soTinChiCanDat;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "ma")
    private String ma;

    @Column(name = "mo_ta")
    private String moTa;

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
