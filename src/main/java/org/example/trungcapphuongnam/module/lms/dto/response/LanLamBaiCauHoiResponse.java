package org.example.trungcapphuongnam.module.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanLamBaiCauHoiResponse {
    private Long id;

    private Long lanLamBaiId;

    private Long cauHoiVersionId;

    private String noiDungSnapshot;

    private LoaiCauHoi loaiCauHoiSnapshot;

    private BigDecimal diemSnapshot;

    private Integer thuTu;

    private LocalDateTime createdAt;

}
