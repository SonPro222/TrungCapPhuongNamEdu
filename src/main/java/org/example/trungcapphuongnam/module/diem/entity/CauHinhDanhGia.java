package org.example.trungcapphuongnam.module.diem.entity;

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
@Table(name = "cau_hinh_danh_gia")
public class CauHinhDanhGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "syllabus_mon_hoc_id", nullable = false)
    private Long syllabusMonHocId;

    @Column(name = "ten_cot_diem", nullable = false)
    private String tenCotDiem;

    @Column(name = "loai_diem", nullable = false, length = 50)
    private String loaiDiem;

    @Column(name = "ty_le", nullable = false, precision = 5, scale = 2)
    private BigDecimal tyLe;

    @Column(name = "diem_toi_da", precision = 5, scale = 2)
    private BigDecimal diemToiDa;

    @Column(name = "thu_tu")
    private Integer thuTu;

    @Column(name = "bat_buoc")
    private Boolean batBuoc;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (diemToiDa == null) diemToiDa = BigDecimal.TEN;
        if (batBuoc == null) batBuoc = true;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
        if (diemToiDa == null) diemToiDa = BigDecimal.TEN;
        if (batBuoc == null) batBuoc = true;
    }
}
