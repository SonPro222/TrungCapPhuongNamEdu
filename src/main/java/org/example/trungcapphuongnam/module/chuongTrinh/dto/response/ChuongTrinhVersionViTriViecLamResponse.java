package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionViTriViecLamResponse {

    private Long id;
    private Long chuongTrinhVersionId;
    private Long viTriGocId;
    private String ma;
    private String ten;
    private String moTa;
    private Integer thuTu;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

