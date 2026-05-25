package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;

import java.time.*;

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
    private LocalDate ngayHoc;
    private String noiDungBuoiHoc;
    private TrangThaiLichHoc trangThai;
    private String ghiChu;
}
