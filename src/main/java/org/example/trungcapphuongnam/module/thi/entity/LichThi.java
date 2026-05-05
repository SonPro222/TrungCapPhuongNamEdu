package org.example.trungcapphuongnam.module.thi.entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Thi.LichThiHinhThuc;
import org.example.trungcapphuongnam.common.enums.Thi.LichThiTrangThai;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lich_thi")
public class LichThi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lop_hoc_phan_id", nullable = false)
    private Long lopHocPhanId;

    @Column(name = "bai_kiem_tra_id")
    private Long baiKiemTraId;

    @Column(name = "ngay_thi", nullable = false)
    private LocalDate ngayThi;

    @Column(name = "ca_hoc_id")
    private Long caHocId;

    @Column(name = "phong_hoc_id")
    private Long phongHocId;

    @Enumerated(EnumType.STRING)
    @Column(name = "hinh_thuc_thi", length = 50)
    private LichThiHinhThuc hinhThucThi;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private LichThiTrangThai trangThai;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (trangThai == null) trangThai = LichThiTrangThai.du_kien;
    }
}
