package org.example.trungcapphuongnam.module.thi.dto.response;


import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.thi.enums.LichThiHinhThuc;
import org.example.trungcapphuongnam.module.thi.enums.LichThiTrangThai;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichThiResponse {
    private Long id;
    private Long lopHocPhanId;
    private Long baiKiemTraId;
    private LocalDate ngayThi;
    private Long caHocId;
    private Long phongHocId;
    private LichThiHinhThuc hinhThucThi;
    private LichThiTrangThai trangThai;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
