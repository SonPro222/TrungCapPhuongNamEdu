package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class XepLichHangLoatResponse {
    private Integer tongLop;
    private Integer soLopThanhCong;
    private Integer soLopLoi;
    private Boolean chiPreview;
    private List<XepLichHangLoatItemResponse> items;
}
