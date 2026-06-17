package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class XepLichHangLoatRequest extends SinhLichHocRequest {
    private List<Long> lopHocPhanIds;
    private Boolean chiPreview = true;
}
