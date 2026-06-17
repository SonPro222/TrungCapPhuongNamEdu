package org.example.trungcapphuongnam.module.daoTao.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NganhLoaiChuongTrinhRequest {

    @NotNull(message = "Ngành không được để trống")
    private Long nganhId;

    @NotNull(message = "Loại chương trình không được để trống")
    private Long loaiChuongTrinhId;

    private String trangThai;

    private String ghiChu;
}
