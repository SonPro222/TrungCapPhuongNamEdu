package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionMucTieuRequest {

    private Long chuongTrinhVersionId;
    private Long mucTieuGocId;
    private Integer thuTu;
    private String ghiChu;
}
