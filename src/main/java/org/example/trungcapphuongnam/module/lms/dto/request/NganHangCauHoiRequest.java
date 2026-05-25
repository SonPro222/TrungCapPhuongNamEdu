package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.MucDoCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.NguonTao;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiSuDung;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NganHangCauHoiRequest {

    @NotNull

    private Long chuongTrinhMonId;

    @NotNull

    private Long nguoiTaoTaiKhoanId;

    private NguonTao nguonTao;

    @NotBlank

    private String noiDung;

    @NotNull

    private LoaiCauHoi loaiCauHoi;

    private MucDoCauHoi mucDo;

    @DecimalMin(value = "0")

    private BigDecimal diemMacDinh;

    private String giaiThichDapAn;

    private TrangThaiSuDung trangThai;

}
