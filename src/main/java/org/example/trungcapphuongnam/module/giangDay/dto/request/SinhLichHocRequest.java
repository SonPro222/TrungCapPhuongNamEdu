package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class SinhLichHocRequest {
    private Long giaoVienId;
    private Long phongHocId;
    private Long caHocId;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private List<Integer> thuTrongTuan;
    private Boolean tuDongGanNoiDungSyllabus = true;
    private Boolean tuDongChonPhong = false;
    private Boolean tuDongChonCa = false;
    private Integer soPhuongAn = 5;
}
