package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiMucTieuChuongTrinh;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MucTieuChuongTrinhGocRequest {

    private String ma;
    private LoaiMucTieuChuongTrinh loai;
    private String noiDung;
    private String ghiChu;
}
