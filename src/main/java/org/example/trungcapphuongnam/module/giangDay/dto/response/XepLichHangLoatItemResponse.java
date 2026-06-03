package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class XepLichHangLoatItemResponse {
    private Long lopHocPhanId;
    private String maLop;
    private String tenLop;
    private Boolean thanhCong;
    private String thongBao;
    private GoiYLichHocResponse phuongAnTotNhat;
    private List<LichHocResponse> lichDaTao;
}
