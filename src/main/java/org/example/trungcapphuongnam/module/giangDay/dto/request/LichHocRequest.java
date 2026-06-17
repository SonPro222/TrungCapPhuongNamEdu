package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;

import java.time.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichHocRequest {
    private Long lopHocPhanId;
    private Long giaoVienId;
    private Long phongHocId;
    private Long caHocId;
    private List<Long> caHocIds;
    private Integer thuTrongTuan;
    private LocalDate ngayHoc;
    private String noiDungBuoiHoc;
    private TrangThaiLichHoc trangThai;
    private String ghiChu;

    /**
     * Cho phép lưu lịch học sau ngày kết thúc gốc của lớp/kỳ khi lịch được sinh tự động và có cấu hình lan kỳ.
     */
    private Boolean choPhepVuotNgayKetThuc;

    private Boolean vuotKhungKy;
    private LocalDate ngayKetThucGoc;
    private Integer soNgayVuotKhungKy;
}
