package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;
import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.chuongTrinh.constant.LoaiMucTieuChuongTrinh;

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
