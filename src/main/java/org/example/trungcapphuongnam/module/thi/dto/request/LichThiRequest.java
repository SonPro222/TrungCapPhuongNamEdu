package org.example.trungcapphuongnam.module.thi.dto.request;


import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.trungcapphuongnam.module.thi.enums.LichThiHinhThuc;
import org.example.trungcapphuongnam.module.thi.enums.LichThiTrangThai;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichThiRequest {
    @NotNull
    private Long lopHocPhanId;
    private Long baiKiemTraId;
    @NotNull
    private LocalDate ngayThi;
    private Long caHocId;
    private Long phongHocId;
    private LichThiHinhThuc hinhThucThi;
    private LichThiTrangThai trangThai;
    private String ghiChu;
}
