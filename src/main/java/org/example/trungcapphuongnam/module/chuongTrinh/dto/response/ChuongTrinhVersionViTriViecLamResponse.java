package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionViTriViecLamResponse {

    private Long id;
    private Long chuongTrinhVersionId;
    private Long viTriGocId;
    private Integer thuTu;
    private String ghiChu;
}
