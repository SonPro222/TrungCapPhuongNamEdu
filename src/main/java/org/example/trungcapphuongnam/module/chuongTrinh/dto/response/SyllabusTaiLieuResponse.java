package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;


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

}
