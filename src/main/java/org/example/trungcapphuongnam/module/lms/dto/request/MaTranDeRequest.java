package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiMaTranDe;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaTranDeRequest {
    private String tenMaTran;

    private Long chuongTrinhMonId;

    private BigDecimal tongDiem;

    private Integer thoiGianLamBai;

    private TrangThaiMaTranDe trangThai;

}
