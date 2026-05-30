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
@Table(name = "syllabus_tai_lieu")
public class SyllabusTaiLieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "syllabus_mon_id")
    private Long syllabusMonId;
    @Column(name = "bat_buoc")
    private Boolean batBuoc;

    @Column(name = "thu_tu")
    private Integer thuTu;
    @Column(name = "ten")
    private String ten;

    @Column(name = "tac_gia")
    private String tacGia;

    @Column(name = "nam_xuat_ban")
    private Integer namXuatBan;

    @Column(name = "nha_xuat_ban")
    private String nhaXuatBan;

    @Column(name = "loai")
    private String loai;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "ma")
    private String ma;

    @Column(name = "duong_dan")
    private String duongDan;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (this.createdAt == null) {
            this.createdAt = now;
        }

        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
