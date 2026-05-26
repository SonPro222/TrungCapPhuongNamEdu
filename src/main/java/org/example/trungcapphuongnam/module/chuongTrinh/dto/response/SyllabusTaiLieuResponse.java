package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusTaiLieuResponse {

    private Long id;

    private Long syllabusMonId;

    private String ten;

    private String tacGia;

    private Integer namXuatBan;

    private String nhaXuatBan;

    private String loai;

    private String ghiChu;

    private String ma;

    private String duongDan;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
