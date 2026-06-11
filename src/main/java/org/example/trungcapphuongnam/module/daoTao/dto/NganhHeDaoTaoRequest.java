package org.example.trungcapphuongnam.module.daoTao.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NganhHeDaoTaoRequest {

    @NotNull(message = "Ngành không được để trống")
    private Long nganhId;

    @NotNull(message = "Trình độ không được để trống")
    private Long trinhDoId;

    @NotNull(message = "Loại chương trình không được để trống")
    private Long loaiChuongTrinhId;

    private String maHe;

    private String tenHe;

    private Integer soThang;

    private Integer soKy;

    private String trangThai;

    private String ghiChu;
}
