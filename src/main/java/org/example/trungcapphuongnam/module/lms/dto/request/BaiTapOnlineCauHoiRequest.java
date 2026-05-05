package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiTapOnlineCauHoiRequest {

    @NotNull

    private Long baiTapOnlineId;

    @NotNull

    private Long cauHoiId;

    @DecimalMin(value = "0")

    private BigDecimal diem;

    private Integer thuTu;

    private Boolean batBuoc;

}
