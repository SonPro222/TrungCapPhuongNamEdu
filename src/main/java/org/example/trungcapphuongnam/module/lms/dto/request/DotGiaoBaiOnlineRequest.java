package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.NguonGiao;
import org.example.trungcapphuongnam.module.lms.enums.PhamViGiaoBai;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DotGiaoBaiOnlineRequest {

    @NotNull

    private Long baiTapOnlineId;



    private NguonGiao nguonGiao;

    private PhamViGiaoBai phamVi;


    private Long nguoiGiaoTaiKhoanId;

    private Long nganhId;

    private Long chuongTrinhVersionId;


    private String ghiChu;

}
