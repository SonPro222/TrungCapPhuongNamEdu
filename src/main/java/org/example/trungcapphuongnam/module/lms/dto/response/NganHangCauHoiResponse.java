package org.example.trungcapphuongnam.module.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class NganHangCauHoiResponse {

    private Long id;

    private Long chuongTrinhMonId;

    private Long nguoiTaoTaiKhoanId;

    private NguonTao nguonTao;

    private String noiDung;

    private LoaiCauHoi loaiCauHoi;

    private MucDoCauHoi mucDo;

    private BigDecimal diemMacDinh;

    private String giaiThichDapAn;

    private TrangThaiSuDung trangThai;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
