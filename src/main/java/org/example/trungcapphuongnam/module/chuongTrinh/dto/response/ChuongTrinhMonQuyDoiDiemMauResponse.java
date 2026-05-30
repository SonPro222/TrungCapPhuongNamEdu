package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhMonQuyDoiDiemMauResponse {

    private Long id;
    private Long chuongTrinhMonId;
    private Long quyDoiDiemMauId;
    private String ghiChu;
}
