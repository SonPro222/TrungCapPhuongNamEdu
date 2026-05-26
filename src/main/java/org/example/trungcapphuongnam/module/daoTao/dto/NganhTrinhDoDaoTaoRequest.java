package org.example.trungcapphuongnam.module.daoTao.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NganhTrinhDoDaoTaoRequest {

    @NotNull(message = "Ngành không được để trống")
    private Long nganhId;

    @NotNull(message = "Trình độ đào tạo không được để trống")
    private Long trinhDoId;

    private String trangThai;

    private String ghiChu;
}
