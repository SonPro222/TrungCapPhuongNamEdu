package org.example.trungcapphuongnam.module.diem.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiKiemTraResponse {
    private Long id;

    private Long lopHocPhanId;

    private Long cauHinhDanhGiaId;

    private Long giaoVienId;

    private String tieuDe;

    private String loaiBai;

    private String moTa;

    private String noiDungDe;

    private LocalDateTime thoiGianBatDau;

    private LocalDateTime thoiGianKetThuc;

    private String hinhThuc;

    private String trangThai;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
