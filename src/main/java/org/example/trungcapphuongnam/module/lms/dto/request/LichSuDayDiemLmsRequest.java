package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.HanhDongDayDiemLms;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuDayDiemLmsRequest {

    @NotNull

    private Long ketQuaBaiTapLopId;

    @NotNull

    private Long diemChiTietId;

    @NotNull

    private Long nguoiThucHienId;

    @DecimalMin(value = "0")

    private BigDecimal diemCu;

    @DecimalMin(value = "0")

    private BigDecimal diemMoi;

    private HanhDongDayDiemLms hanhDong;

    private String ghiChu;

}
