package org.example.trungcapphuongnam.module.sinhVien.entity;


import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.sinhVien.enums.MienTruBaoLuuLoai;
import org.example.trungcapphuongnam.module.sinhVien.enums.PheDuyetTrangThai;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mien_tru_bao_luu")
public class MienTruBaoLuu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sinh_vien_id", nullable = false)
    private SinhVien sinhVien;

    @Column(name = "chuong_trinh_mon_id", nullable = false)
    private Long chuongTrinhMonId;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai", nullable = false, length = 50)
    private MienTruBaoLuuLoai loai;

    @Column(name = "ly_do", columnDefinition = "TEXT")
    private String lyDo;

    @Column(name = "minh_chung", columnDefinition = "TEXT")
    private String minhChung;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", length = 50)
    private PheDuyetTrangThai trangThai;

    @Column(name = "nguoi_duyet_id")
    private Long nguoiDuyetId;

    @Column(name = "ngay_duyet")
    private LocalDateTime ngayDuyet;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (trangThai == null) {
            trangThai = PheDuyetTrangThai.cho_duyet;
        }
    }
}
