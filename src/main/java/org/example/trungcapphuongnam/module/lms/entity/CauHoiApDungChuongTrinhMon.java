package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiApDung;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cau_hoi_ap_dung_chuong_trinh_mon")
public class CauHoiApDungChuongTrinhMon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cau_hoi_version_id", nullable = false)
    private Long cauHoiVersionId;

    @Column(name = "chuong_trinh_mon_id", nullable = false)
    private Long chuongTrinhMonId;

    @Column(name = "syllabus_chuong_bai_id")
    private Long syllabusChuongBaiId;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private TrangThaiCauHoiApDung trangThai;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
