package org.example.trungcapphuongnam.module.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.HanhDongDayDiemLms;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuDayDiemLmsResponse {

    private Long id;

    private Long ketQuaBaiTapLopId;

    private Long diemChiTietId;

    private Long nguoiThucHienId;

    private BigDecimal diemCu;

    private BigDecimal diemMoi;

    private HanhDongDayDiemLms hanhDong;

    private String ghiChu;

    private LocalDateTime createdAt;

}
