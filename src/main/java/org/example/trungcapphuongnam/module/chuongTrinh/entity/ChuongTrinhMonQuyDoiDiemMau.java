package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chuong_trinh_mon_quy_doi_diem_mau")
public class ChuongTrinhMonQuyDoiDiemMau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Chỉ là khóa cha được suy ra từ syllabus_mon_hoc_id để giữ tương thích report/filter cũ.
     * Không nhận trực tiếp từ request nghiệp vụ.
     */
    @Column(name = "chuong_trinh_mon_id")
    private Long chuongTrinhMonId;

    @Column(name = "syllabus_mon_hoc_id", nullable = false)
    private Long syllabusMonHocId;

    @Column(name = "quy_doi_diem_mau_id", nullable = false)
    private Long quyDoiDiemMauId;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
}
