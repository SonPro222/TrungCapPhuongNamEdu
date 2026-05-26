package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LopHocPhanResponse {
    private Long id;
    private Long chuongTrinhMonId;
    private Long monHocId;
    private LoaiLopHocPhan loaiLopHocPhan;
    private Integer siSoToiThieu;
    private String maLop;
    private String tenLop;
    private Integer soLuongToiDa;
    private Integer soLuongHienTai;
    private Integer soBuoiHoc;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;
    private TrangThaiLopHocPhan trangThai;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
