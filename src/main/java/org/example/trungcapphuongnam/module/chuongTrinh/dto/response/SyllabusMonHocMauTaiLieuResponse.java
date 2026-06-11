package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;


import lombok.*;

        import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocMauTaiLieuResponse {

    private Long id;

    private Long syllabusMonHocMauId;

    private Long taiLieuMauId;
    private String ma;

    private String ten;

    private String loai;

    private String tacGia;

    private String nhaXuatBan;

    private Integer namXuatBan;

    private String duongDan;


    private Integer thuTu;

    private Boolean batBuoc;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}