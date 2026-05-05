package org.example.trungcapphuongnam.module.lms.dto.response;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Lms.NguonGiao;
import org.example.trungcapphuongnam.common.enums.Lms.PhamViGiaoBai;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DotGiaoBaiOnlineResponse {

    private Long id;

    private Long baiTapOnlineId;

    private Long nguoiGiaoTaiKhoanId;

    private NguonGiao nguonGiao;

    private PhamViGiaoBai phamVi;

    private Long nganhId;

    private Long chuongTrinhVersionId;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
