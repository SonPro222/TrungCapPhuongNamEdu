package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;


import lombok.*;

        import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocGocDieuKienResponse {

    private Long id;

    private Long syllabusMonHocGocId;

    private Long dieuKienGocId;

    private Integer thuTu;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}