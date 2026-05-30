package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.VaiTroThucHienLms;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuChinhDiemBaiTapRequest {

    @NotNull

    private Long ketQuaBaiTapLopId;

    @NotNull

    private Long nguoiChinhId;

    private VaiTroThucHienLms vaiTroChinh;

    @DecimalMin(value = "0")

    private BigDecimal diemCu;

    @DecimalMin(value = "0")

    private BigDecimal diemMoi;

    @NotBlank

    private String lyDo;

}
