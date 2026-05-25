package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiNangLucDauRa;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NangLucDauRaGocResponse {

    private Long id;
    private String ma;
    private LoaiNangLucDauRa loai;
    private String noiDung;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
