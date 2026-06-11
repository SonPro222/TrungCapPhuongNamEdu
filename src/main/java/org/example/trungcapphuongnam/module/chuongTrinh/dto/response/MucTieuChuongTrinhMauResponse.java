package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiMucTieuChuongTrinh;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MucTieuChuongTrinhMauResponse {

    private Long id;
    private Long syllabusChuongTrinhMauId;
    private String ma;
    private LoaiMucTieuChuongTrinh loai;
    private String noiDung;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
