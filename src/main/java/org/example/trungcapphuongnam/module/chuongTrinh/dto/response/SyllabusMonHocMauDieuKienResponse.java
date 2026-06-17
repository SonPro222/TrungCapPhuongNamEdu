package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;


import lombok.*;

        import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocMauDieuKienResponse {

    private Long id;

    private Long syllabusMonHocMauId;

    private Long dieuKienMauId;

    private Integer thuTu;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}