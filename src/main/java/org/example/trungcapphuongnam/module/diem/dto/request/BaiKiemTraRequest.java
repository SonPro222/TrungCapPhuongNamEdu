package org.example.trungcapphuongnam.module.diem.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiKiemTraRequest {
    @NotNull(message = "lopHocPhanId không được để trống")
    private Long lopHocPhanId;

    private Long cauHinhDanhGiaId;

    private Long giaoVienId;

    @NotBlank(message = "tieuDe không được để trống")
    private String tieuDe;

    @NotBlank(message = "loaiBai không được để trống")
    private String loaiBai;

    private String moTa;

    private String noiDungDe;

    private LocalDateTime thoiGianBatDau;

    private LocalDateTime thoiGianKetThuc;

    private String hinhThuc;

    private String trangThai;


}
