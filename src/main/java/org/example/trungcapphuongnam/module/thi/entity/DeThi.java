package org.example.trungcapphuongnam.module.thi.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.thi.enums.DeThiTrangThai;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "de_thi")
public class DeThi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lich_thi_id")
    private Long lichThiId;

    @Column(name = "lop_hoc_phan_id")
    private Long lopHocPhanId;

    @Column(name = "giao_vien_id")
    private Long giaoVienId;

    @Column(name = "ma_de", length = 50)
    private String maDe;

    @Column(name = "tieu_de", length = 255)
    private String tieuDe;

    @Column(name = "noi_dung_de", columnDefinition = "TEXT")
    private String noiDungDe;

    @Column(name = "file_de_thi", columnDefinition = "TEXT")
    private String fileDeThi;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private DeThiTrangThai trangThai;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (trangThai == null) trangThai = DeThiTrangThai.nhap;
    }
}
