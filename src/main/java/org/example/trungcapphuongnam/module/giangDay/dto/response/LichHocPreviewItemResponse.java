package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class LichHocPreviewItemResponse {
    private Integer stt;
    private LocalDate ngayHoc;
    private Long caHocId;
    private Long phongHocId;
    private Long giaoVienId;
    private String noiDungBuoiHoc;
    private List<String> canhBao;

    /**
     * true nếu buổi học được bù sau ngày kết thúc gốc của khoảng sinh lịch/kỳ.
     */
    private Boolean vuotKhungKy;
    private LocalDate ngayKetThucGoc;
    private Integer soNgayVuotKhungKy;
}
