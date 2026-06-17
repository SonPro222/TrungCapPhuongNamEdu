package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionDieuKienTotNghiepRequest {

    private Long chuongTrinhVersionId;
    private Long dieuKienMauId;
    private Integer thuTu;
    private String ghiChu;
}
