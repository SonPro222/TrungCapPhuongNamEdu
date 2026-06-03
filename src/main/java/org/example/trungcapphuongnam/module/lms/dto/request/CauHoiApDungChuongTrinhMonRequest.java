package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiApDung;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHoiApDungChuongTrinhMonRequest {
    private Long cauHoiVersionId;

    private Long chuongTrinhMonId;

    private Long syllabusChuongBaiId;

    private TrangThaiCauHoiApDung trangThai;

    private String ghiChu;

}
