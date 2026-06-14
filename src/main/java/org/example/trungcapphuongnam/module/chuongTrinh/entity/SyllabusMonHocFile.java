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
@Table(name = "syllabus_mon_hoc_file")
public class SyllabusMonHocFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "syllabus_mon_hoc_id", nullable = false)
    private Long syllabusMonHocId;

    @Column(name = "ten_file", nullable = false, length = 255)
    private String tenFile;

    @Column(name = "loai_file", length = 100)
    private String loaiFile;

    @Column(name = "kich_thuoc")
    private Long kichThuoc;

    @Column(name = "du_lieu", columnDefinition = "BYTEA")
    private byte[] duLieu;

    @Column(name = "noi_dung_text", columnDefinition = "TEXT")
    private String noiDungText;

    @Column(name = "loai_tai_lieu", length = 50)
    private String loaiTaiLieu;

    @Column(name = "la_file_nguon")
    private Boolean laFileNguon;

    @Column(name = "trang_thai_doc", length = 50)
    private String trangThaiDoc;

    @Column(name = "loi_doc", columnDefinition = "TEXT")
    private String loiDoc;

    @Column(name = "checksum", length = 128)
    private String checksum;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
