package org.example.trungcapphuongnam.module.sinhVien.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinhVienDiHocLaiRequest {

    @NotNull(message = "Ngày đi học lại không được để trống")
    private LocalDate ngayDiHocLai;

    @NotNull(message = "Version học lại không được để trống")
    private Long chuongTrinhVersionIdHocLai;

    @NotNull(message = "Kỳ học lại không được để trống")
    private Long khungKyIdHocLai;

    @NotNull(message = "Lớp hành chính học lại không được để trống")
    private Long lopHanhChinhIdHocLai;

    private String ghiChu;
}