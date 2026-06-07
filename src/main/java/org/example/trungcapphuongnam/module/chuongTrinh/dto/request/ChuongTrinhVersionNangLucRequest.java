package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiNangLucDauRa;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionNangLucRequest {

    private Long chuongTrinhVersionId;
    private Long nangLucGocId;
    private String ma;
    private LoaiNangLucDauRa loai;
    private String noiDung;
    private Integer thuTu;
    private String ghiChu;
}
