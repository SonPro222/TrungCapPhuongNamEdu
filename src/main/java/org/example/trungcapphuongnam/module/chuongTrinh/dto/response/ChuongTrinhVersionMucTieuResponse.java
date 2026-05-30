package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionMucTieuResponse {

    private Long id;
    private Long chuongTrinhVersionId;
    private Long mucTieuGocId;
    private Integer thuTu;
    private String ghiChu;
}
