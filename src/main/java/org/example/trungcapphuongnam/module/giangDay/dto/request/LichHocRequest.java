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
}
