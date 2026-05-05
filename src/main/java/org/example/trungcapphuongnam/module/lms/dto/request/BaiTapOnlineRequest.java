package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Lms.LoaiBaiOnline;
import org.example.trungcapphuongnam.common.enums.Lms.NguonTao;
import org.example.trungcapphuongnam.common.enums.Lms.TrangThaiBaiTapOnline;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiTapOnlineRequest {

    @NotNull

    private Long chuongTrinhMonId;

    @NotNull

    private Long nguoiTaoTaiKhoanId;

    private NguonTao nguonTao;

    @NotBlank

    private String tieuDe;

    private String moTa;

    private LoaiBaiOnline loaiBai;

    @DecimalMin(value = "0")

    private BigDecimal tongDiem;

    private TrangThaiBaiTapOnline trangThai;

}
