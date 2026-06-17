package org.example.trungcapphuongnam.module.lms.dto.response;

import java.math.BigDecimal;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiMaTranDe;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaTranDeResponse {
    private Long id;

    private String tenMaTran;

    private Long chuongTrinhMonId;

    private BigDecimal tongDiem;

    private Integer thoiGianLamBai;

    private TrangThaiMaTranDe trangThai;

}
