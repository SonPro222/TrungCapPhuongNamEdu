package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiYeuCauDoiLich;

import java.time.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YeuCauDoiLichRequest {
    private Long lichHocCuId;
    private Long giaoVienId;
    private LocalDate ngayMoi;
    private Long caHocMoiId;
    private Long phongHocMoiId;
    private String lyDo;
    private TrangThaiYeuCauDoiLich trangThai;
    private Long nguoiDuyetId;
    private LocalDateTime ngayDuyet;
}
