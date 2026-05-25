package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LopHocPhanChuongTrinhMonRequest {
    private Long lopHocPhanId;
    private Long chuongTrinhMonId;
    private String ghiChu;
}