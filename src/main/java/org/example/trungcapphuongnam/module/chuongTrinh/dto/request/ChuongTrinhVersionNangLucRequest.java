package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionNangLucRequest {

    private Long chuongTrinhVersionId;
    private Long nangLucGocId;
    private Integer thuTu;
    private String ghiChu;
}
