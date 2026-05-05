package org.example.trungcapphuongnam.module.lms.dto.response;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiTapOnlineCauHoiResponse {

    private Long id;

    private Long baiTapOnlineId;

    private Long cauHoiId;

    private BigDecimal diem;

    private Integer thuTu;

    private Boolean batBuoc;

    private LocalDateTime createdAt;

}
