package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.MucDoCauHoi;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaTranDeChiTietRequest {
    private Long maTranDeId;

    private Long syllabusChuongBaiId;

    private MucDoCauHoi mucDo;

    private LoaiCauHoi loaiCauHoi;

    private Integer soCau;

    private BigDecimal diemMoiCau;

}
