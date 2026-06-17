package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DieuKienTotNghiepMauRequest {

    private Long syllabusChuongTrinhMauId;

    private String ma;
    private String noiDung;
    private String ghiChu;
}
