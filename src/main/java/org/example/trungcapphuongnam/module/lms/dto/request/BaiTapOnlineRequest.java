package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.LoaiBaiOnline;
import org.example.trungcapphuongnam.module.lms.enums.NguonTao;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiBaiTapOnline;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiTapOnlineRequest {
    @NotNull
    private Long chuongTrinhMonId;

    private Long nguoiTaoTaiKhoanId;

    private NguonTao nguonTao;

    @NotBlank
    private String tieuDe;

    private String moTa;

    private LoaiBaiOnline loaiBai;

    @DecimalMin(value = "0")
    private BigDecimal tongDiem;

    private TrangThaiBaiTapOnline trangThai;

    private Boolean tronCauHoi;

    private Boolean tronDapAn;

    private Boolean sinhDeTuDong;

    private Boolean hienDapAnSauKhiNop;

    private Boolean hienGiaiThichSauKhiNop;

    private Long maTranDeId;
}
