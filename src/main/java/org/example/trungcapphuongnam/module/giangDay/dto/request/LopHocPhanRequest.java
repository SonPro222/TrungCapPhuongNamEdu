package org.example.trungcapphuongnam.module.giangDay.dto.request;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LopHocPhanRequest {
    private Long chuongTrinhMonId;
    private String maLop;
    private String tenLop;
    private Integer soLuongToiDa;
    private Integer soLuongHienTai;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private Long monHocId;
    private LoaiLopHocPhan loaiLopHocPhan;
    private Integer siSoToiThieu;
    private TrangThaiLopHocPhan trangThai;
}
