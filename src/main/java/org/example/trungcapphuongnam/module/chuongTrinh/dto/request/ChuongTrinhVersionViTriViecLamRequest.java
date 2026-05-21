package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionViTriViecLamRequest {

    private Long chuongTrinhVersionId;
    private Long viTriGocId;
    private Integer thuTu;
    private String ghiChu;
}
