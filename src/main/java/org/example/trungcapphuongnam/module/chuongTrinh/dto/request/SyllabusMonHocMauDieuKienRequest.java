package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocMauDieuKienRequest {

    private Long syllabusMonHocMauId;

    private Long dieuKienMauId;

    private Integer thuTu;

    private String ghiChu;
}