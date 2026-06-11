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
    private Long mucTieuMauId;
    private Integer thuTu;
    private String ghiChu;
}
