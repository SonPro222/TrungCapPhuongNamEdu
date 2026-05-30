package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiemDanhResponse {
    private Long id;

    private Long lichHocId;
    private LocalDate ngayHoc;
    private String noiDungBuoiHoc;

    private Long lopHocPhanId;
    private String maLop;
    private String tenLop;

    private Long sinhVienId;
    private String maSinhVien;
    private String tenSinhVien;

    private TrangThaiDiemDanh trangThai;
    private String ghiChu;
    private LocalDateTime thoiGianDiemDanh;
}