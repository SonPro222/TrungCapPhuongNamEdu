package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GiaoVienMonHocRequest {
    private Long giaoVienId;
    private Long monHocId;
    private String trangThai;
    private String ghiChu;
}
