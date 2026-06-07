package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SinhLichHocPreviewResponse {
    private Boolean hopLe;
    private Integer soBuoiCanXep;
    private Integer soBuoiDaXep;
    private Integer soBuoiConLai;
    private Integer soBuoiXepDuoc;
    private List<String> canhBao;
    private List<LichHocPreviewItemResponse> items;
}
