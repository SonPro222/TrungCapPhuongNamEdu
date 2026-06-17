package org.example.trungcapphuongnam.module.lms.dto.response;

import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiApDung;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHoiApDungChuongTrinhMonResponse {
    private Long id;

    private Long cauHoiVersionId;

    private Long chuongTrinhMonId;

    private Long syllabusChuongBaiId;

    private TrangThaiCauHoiApDung trangThai;

    private String ghiChu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
