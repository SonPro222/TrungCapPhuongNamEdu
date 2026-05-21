package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionNangLucResponse {

    private Long id;
    private Long chuongTrinhVersionId;
    private Long nangLucGocId;
    private Integer thuTu;
    private String ghiChu;
}
