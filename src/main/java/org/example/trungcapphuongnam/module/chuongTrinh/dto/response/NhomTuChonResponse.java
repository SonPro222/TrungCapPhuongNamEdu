package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhomTuChonResponse {

    private Long id;

    private Long chuongTrinhVersionId;
    private Long nhomTuChonGocId;
    private String ten;

    private Integer soMonChon;

    private BigDecimal soTinChiCanDat;

    private String ghiChu;

}
