package org.example.trungcapphuongnam.module.chuongTrinh.entity;


import jakarta.persistence.*;
        import lombok.*;
        import org.example.trungcapphuongnam.module.chuongTrinh.constant.LoaiDieuKienMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.constant.LoaiDieuKienMonHocConverter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dieu_kien_mon_hoc_goc")
public class DieuKienMonHocGoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma", length = 50)
    private String ma;

    @Convert(converter = LoaiDieuKienMonHocConverter.class)
    @Column(name = "loai", nullable = false, length = 50)
    private LoaiDieuKienMonHoc loai;

    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}