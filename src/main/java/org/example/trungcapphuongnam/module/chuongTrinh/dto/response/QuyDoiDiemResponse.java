package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuyDoiDiemResponse {

    private Long id;

    private Long chuongTrinhMonId;

    private BigDecimal nguongTu;

    private BigDecimal nguongDen;

    private BigDecimal diemQuyDoi;

    private String ketQua;

    private String congThuc;

    private String ghiChu;

}
