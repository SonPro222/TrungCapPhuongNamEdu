package org.example.trungcapphuongnam.module.lms.dto.response;

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
    private Long cauHoiVersionId;
    private BigDecimal diem;
    private Integer thuTu;
    private Boolean batBuoc;
    private LocalDateTime createdAt;
}
