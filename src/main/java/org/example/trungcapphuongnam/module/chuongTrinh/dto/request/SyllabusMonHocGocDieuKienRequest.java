package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocGocDieuKienRequest {

    private Long syllabusMonHocGocId;

    private Long dieuKienGocId;

    private Integer thuTu;

    private String ghiChu;
}