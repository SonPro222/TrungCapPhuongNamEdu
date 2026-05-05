package org.example.trungcapphuongnam.module.thi.dto.request;


import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.Thi.LichThiHinhThuc;
import org.example.trungcapphuongnam.common.enums.Thi.LichThiTrangThai;

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
