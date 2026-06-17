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
    private List<Long> caHocIds;
    private LocalDate tuNgay;
    private LocalDate denNgay;
    private List<Integer> thuTrongTuan;
    private Boolean tuDongGanNoiDungSyllabus = true;
    private Boolean tuDongChonPhong = false;
    private Boolean tuDongChonCa = false;
    private Integer soPhuongAn = 5;

    /**
     * true: ngày nghỉ không được xếp học; nếu thiếu buổi thì tiếp tục tìm ngày học hợp lệ tiếp theo.
     * Hiện tại sinh lịch luôn né ngày nghỉ, field này dùng để FE thể hiện rõ nghiệp vụ.
     */
    private Boolean tuDongBuNgayNghi = true;

    /**
     * true: nếu hết denNgay mà chưa đủ số buổi thì được tiếp tục xếp sang sau denNgay.
     */
    private Boolean choPhepLanKy = false;

    /**
     * Số ngày tối đa được xếp vượt quá denNgay khi choPhepLanKy=true.
     */
    private Integer soNgayLanKyToiDa = 0;
}
