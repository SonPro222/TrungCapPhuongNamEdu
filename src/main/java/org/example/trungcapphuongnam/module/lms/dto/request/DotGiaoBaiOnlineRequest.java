package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Lms.NguonGiao;
import org.example.trungcapphuongnam.common.enums.Lms.PhamViGiaoBai;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DotGiaoBaiOnlineRequest {

    @NotNull

    private Long baiTapOnlineId;

    @NotNull

    private Long nguoiGiaoTaiKhoanId;

    private NguonGiao nguonGiao;

    private PhamViGiaoBai phamVi;

    @NotNull

    private Long nganhId;

    @NotNull

    private Long chuongTrinhVersionId;

    private String ghiChu;

}
