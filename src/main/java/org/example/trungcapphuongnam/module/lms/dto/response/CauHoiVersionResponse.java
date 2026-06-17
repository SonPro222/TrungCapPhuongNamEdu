package org.example.trungcapphuongnam.module.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.MucDoCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiVersion;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHoiVersionResponse {
    private Long id;

    private Long cauHoiMauId;

    private Integer versionNo;

    private String noiDung;

    private LoaiCauHoi loaiCauHoi;

    private MucDoCauHoi mucDo;

    private BigDecimal diemMacDinh;

    private String giaiThichDapAn;

    private TrangThaiCauHoiVersion trangThai;

    private Long nangLucDauRaId;

    private Long nguoiDuyetTaiKhoanId;

    private LocalDateTime thoiGianDuyet;

    private String noiDungHash;

    private Boolean isLocked;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
