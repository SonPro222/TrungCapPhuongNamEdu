package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GoiYLichHocResponse {
    private Integer thuTuPhuongAn;
    private Boolean hopLe;
    private Integer diemUuTien;
    private Long phongHocId;
    private Long caHocId;
    private List<Integer> thuTrongTuan;
    private Integer soBuoiCanXep;
    private Integer soBuoiXepDuoc;
    private Integer soCanhBao;
    private List<String> canhBao;
    private List<LichHocPreviewItemResponse> items;
}
