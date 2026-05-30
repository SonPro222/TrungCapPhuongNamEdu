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
@Table(name = "syllabus_mon_hoc_dieu_kien")
public class SyllabusMonHocDieuKien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "syllabus_mon_id", nullable = false)
    private Long syllabusMonId;

    @Column(name = "dieu_kien_goc_id", nullable = false)
    private Long dieuKienGocId;

    @Column(name = "thu_tu")
    private Integer thuTu;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}