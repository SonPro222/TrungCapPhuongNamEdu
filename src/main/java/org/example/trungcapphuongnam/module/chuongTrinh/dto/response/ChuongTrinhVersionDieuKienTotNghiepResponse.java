package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionDieuKienTotNghiepResponse {

    private Long id;
    private Long chuongTrinhVersionId;
    private Long dieuKienMauId;
    private Integer thuTu;
    private String ghiChu;
}
