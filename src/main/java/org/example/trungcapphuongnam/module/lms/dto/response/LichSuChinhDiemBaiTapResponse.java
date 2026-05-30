package org.example.trungcapphuongnam.module.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.VaiTroThucHienLms;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuChinhDiemBaiTapResponse {

    private Long id;

    private Long ketQuaBaiTapLopId;

    private Long nguoiChinhId;

    private VaiTroThucHienLms vaiTroChinh;

    private BigDecimal diemCu;

    private BigDecimal diemMoi;

    private String lyDo;

    private LocalDateTime createdAt;

}
