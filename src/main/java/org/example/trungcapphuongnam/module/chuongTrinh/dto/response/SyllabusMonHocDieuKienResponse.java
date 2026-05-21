package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;


import lombok.*;

        import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocDieuKienResponse {

    private Long id;

    private Long syllabusMonId;

    private Long dieuKienGocId;

    private Integer thuTu;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}