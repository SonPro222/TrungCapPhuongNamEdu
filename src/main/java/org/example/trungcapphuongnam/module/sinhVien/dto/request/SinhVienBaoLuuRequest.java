package org.example.trungcapphuongnam.module.sinhVien.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVienBaoLuuRequest {

    @NotNull(message = "Sinh viên không được để trống")
    private Long sinhVienId;

    @NotNull(message = "Sinh viên chương trình không được để trống")
    private Long sinhVienChuongTrinhId;

    private LocalDate ngayBatDau;

    private LocalDate ngayKetThucDuKien;

    @NotNull(message = "Số kỳ bảo lưu không được để trống")
    private Integer soKyBaoLuu;

    @NotBlank(message = "Lý do bảo lưu không được để trống")
    private String lyDo;

    private String ghiChu;
}