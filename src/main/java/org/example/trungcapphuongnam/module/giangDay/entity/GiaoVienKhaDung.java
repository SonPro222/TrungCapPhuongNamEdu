package org.example.trungcapphuongnam.module.giangDay.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiDangKyGiangVien;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "giao_vien_kha_dung")
public class GiaoVienKhaDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "giao_vien_id", nullable = false)
    private Long giaoVienId;

    @Column(name = "thu_trong_tuan")
    private Integer thuTrongTuan;

    @Column(name = "ca_hoc_id")
    private Long caHocId;

    @Column(name = "tu_ngay")
    private LocalDate tuNgay;

    @Column(name = "den_ngay")
    private LocalDate denNgay;

    @Column(name = "kha_dung")
    private Boolean khaDung;

    @Column(name = "khung_ky_id")
    private Long khungKyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_dang_ky", length = 50)
    private LoaiDangKyGiangVien loaiDangKy;

    @Column(name = "muc_do_uu_tien")
    private Integer mucDoUuTien;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
