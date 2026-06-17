package org.example.trungcapphuongnam.module.lms.dto.response;

import java.math.BigDecimal;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.MucDoCauHoi;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaTranDeChiTietResponse {
    private Long id;

    private Long maTranDeId;

    private Long syllabusChuongBaiId;

    private MucDoCauHoi mucDo;

    private LoaiCauHoi loaiCauHoi;

    private Integer soCau;

    private BigDecimal diemMoiCau;

}
