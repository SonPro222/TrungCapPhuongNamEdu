package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocTaiLieuResponse {

    private Long id;

    private Long syllabusMonId;

    private Long taiLieuGocId;

    private Integer thuTu;

    private Boolean batBuoc;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}