package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiMucTieuChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiMucTieuChuongTrinhConverter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "muc_tieu_chuong_trinh_mau")
public class MucTieuChuongTrinhMau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "syllabus_chuong_trinh_mau_id", nullable = false)
    private Long syllabusChuongTrinhMauId;


    @Column(name = "ma", nullable = true, length = 50)
    private String ma;
    @Convert(converter = LoaiMucTieuChuongTrinhConverter.class)
    @Column(name = "loai", nullable = false, length = 50)
    private LoaiMucTieuChuongTrinh loai;
    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;
    @Column(name = "ghi_chu", nullable = true, columnDefinition = "TEXT")
    private String ghiChu;
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

}
