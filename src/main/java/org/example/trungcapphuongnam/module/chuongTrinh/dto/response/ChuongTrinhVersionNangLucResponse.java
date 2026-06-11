package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiNangLucDauRa;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhVersionNangLucResponse {

    private Long id;
    private Long chuongTrinhVersionId;
    private Long nangLucMauId;
    private String ma;
    private LoaiNangLucDauRa loai;
    private String noiDung;
    private Integer thuTu;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

