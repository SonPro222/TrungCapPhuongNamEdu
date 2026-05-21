package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhMonQuyDoiDiemMauRequest {

    private Long chuongTrinhMonId;
    private Long quyDoiDiemMauId;
    private String ghiChu;
}
