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
@Table(name = "syllabus_mon_hoc_mau_tai_lieu")
public class SyllabusMonHocMauTaiLieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "syllabus_mon_hoc_mau_id", nullable = false)
    private Long syllabusMonHocMauId;

    @Column(name = "tai_lieu_mau_id", nullable = false)
    private Long taiLieuMauId;


    @Column(name = "ma", length = 100)
    private String ma;

    @Column(name = "ten", length = 500)
    private String ten;

    @Column(name = "loai", length = 100)
    private String loai;

    @Column(name = "tac_gia", length = 255)
    private String tacGia;

    @Column(name = "nha_xuat_ban", length = 255)
    private String nhaXuatBan;

    @Column(name = "nam_xuat_ban")
    private Integer namXuatBan;

    @Column(name = "duong_dan", columnDefinition = "TEXT")
    private String duongDan;

    @Column(name = "thu_tu")
    private Integer thuTu;

    @Column(name = "bat_buoc")
    private Boolean batBuoc;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}