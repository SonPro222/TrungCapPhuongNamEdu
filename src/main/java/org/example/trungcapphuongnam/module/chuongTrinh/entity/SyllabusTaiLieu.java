package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;


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

}
