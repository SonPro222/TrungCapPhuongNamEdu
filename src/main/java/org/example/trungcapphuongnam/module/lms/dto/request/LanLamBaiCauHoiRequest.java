package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanLamBaiCauHoiRequest {
    private Long lanLamBaiId;

    private Long cauHoiVersionId;

    private String noiDungSnapshot;

    private LoaiCauHoi loaiCauHoiSnapshot;

    private BigDecimal diemSnapshot;

    private Integer thuTu;

}
