package org.example.trungcapphuongnam.module.donTu.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "don_tu")
public class DonTu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_don", unique = true, length = 100)
    private String maDon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loai_don_tu_id", nullable = false)
    private LoaiDonTu loaiDonTu;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sinh_vien_id", nullable = false)
    private SinhVien sinhVien;

    @Column(name = "tieu_de", length = 255)
    private String tieuDe;

    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "ly_do", columnDefinition = "TEXT")
    private String lyDo;

    @Column(name = "du_lieu", columnDefinition = "jsonb")
    private String duLieu;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai", nullable = false, length = 50)
    private DonTuTrangThai trangThai;

    @Column(name = "ngay_gui")
    private LocalDateTime ngayGui;

    @Column(name = "ngay_tiep_nhan")
    private LocalDateTime ngayTiepNhan;

    @Column(name = "ngay_duyet")
    private LocalDateTime ngayDuyet;

    @Column(name = "ngay_tu_choi")
    private LocalDateTime ngayTuChoi;

    @Column(name = "ngay_hoan_tat")
    private LocalDateTime ngayHoanTat;

    @Column(name = "nguoi_tiep_nhan_id")
    private Long nguoiTiepNhanId;

    @Column(name = "nguoi_duyet_id")
    private Long nguoiDuyetId;

    @Column(name = "y_kien_xu_ly", columnDefinition = "TEXT")
    private String yKienXuLy;

    @Column(name = "ly_do_tu_choi", columnDefinition = "TEXT")
    private String lyDoTuChoi;

    @Column(name = "doi_tuong_lien_quan_type", length = 100)
    private String doiTuongLienQuanType;

    @Column(name = "doi_tuong_lien_quan_id")
    private Long doiTuongLienQuanId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void prePersist() {
        if (trangThai == null) {
            trangThai = DonTuTrangThai.cho_duyet;
        }
        if (ngayGui == null) {
            ngayGui = LocalDateTime.now();
        }
        if (maDon == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            int rand = new Random().nextInt(9000) + 1000;
            maDon = "DON" + timestamp + rand;
        }
    }
}
