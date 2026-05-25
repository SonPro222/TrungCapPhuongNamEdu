package org.example.trungcapphuongnam.module.heThong.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauHinhMaSinhVienRequest {

    @NotNull(message = "Ngành không được để trống")
    private Long nganhId;

    @NotNull(message = "Version chương trình không được để trống")
    private Long chuongTrinhVersionId;

    @NotBlank(message = "Tiền tố mã sinh viên không được để trống")
    private String tienTo;

    @NotBlank(message = "Mã đầu không được để trống")
    private String maDau;

    private Integer soHienTai;

    @Positive(message = "Độ dài số thứ tự phải lớn hơn 0")
    private Integer doDaiSoThuTu;

    private String ghiChu;
}