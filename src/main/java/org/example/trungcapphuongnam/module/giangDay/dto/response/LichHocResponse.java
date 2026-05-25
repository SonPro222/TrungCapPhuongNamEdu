package org.example.trungcapphuongnam.module.giangDay.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;

import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichHocResponse {
    private Long id;

    private Long lopHocPhanId;
    private String maLop;
    private String tenLop;

    private Long giaoVienId;
    private String maGiaoVien;
    private String tenGiaoVien;

    private Long phongHocId;
    private String maPhong;
    private String tenPhong;

    private Long caHocId;
    private String maCa;
    private String tenCa;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;

    private LocalDate ngayHoc;
    private String noiDungBuoiHoc;
    private TrangThaiLichHoc trangThai;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}