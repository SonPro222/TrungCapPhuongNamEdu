package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;


import lombok.*;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiDieuKienMonHoc;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DieuKienMonHocMauResponse {

    private Long id;

    private String ma;

    private LoaiDieuKienMonHoc loai;

    private String noiDung;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}