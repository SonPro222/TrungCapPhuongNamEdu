package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViTriViecLamMauRequest {

    private Long syllabusChuongTrinhMauId;
    private String ma;
    private String ten;
    private String moTa;
    private String ghiChu;
}
