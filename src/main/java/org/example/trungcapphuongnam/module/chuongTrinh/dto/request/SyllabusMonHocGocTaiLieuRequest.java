package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocGocTaiLieuRequest {

    private Long syllabusMonHocGocId;

    private Long taiLieuGocId;

    private Integer thuTu;

    private Boolean batBuoc;

    private String ghiChu;
}