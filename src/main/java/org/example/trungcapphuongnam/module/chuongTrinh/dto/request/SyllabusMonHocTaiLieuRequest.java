package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocTaiLieuRequest {

    private Long syllabusMonId;

    private Long taiLieuGocId;

    private Integer thuTu;

    private Boolean batBuoc;

    private String ghiChu;
}