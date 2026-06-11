package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViTriViecLamMauResponse {

    private Long id;
    private Long syllabusChuongTrinhMauId;
    private String ma;
    private String ten;
    private String moTa;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
