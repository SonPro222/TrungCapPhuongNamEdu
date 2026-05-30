package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocDieuKienRequest {

    private Long syllabusMonId;

    private Long dieuKienGocId;

    private Integer thuTu;

    private String ghiChu;
}