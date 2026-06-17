package org.example.trungcapphuongnam.module.lms.dto.response;

import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.NguonGiao;
import org.example.trungcapphuongnam.module.lms.enums.PhamViGiaoBai;

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
