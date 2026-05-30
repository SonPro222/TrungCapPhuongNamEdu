package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhRequest {

    @NotNull(message = "loại chương trình theo ngành không được để trống")
    private Long nganhLoaiChuongTrinhId;

    private Long trinhDoId;

    @NotBlank(message = "ma_chuong_trinh không được để trống")
    private String maChuongTrinh;

    @NotBlank(message = "ten_chuong_trinh không được để trống")
    private String tenChuongTrinh;

    private String doiTuongTuyenSinh;

    private String thoiGianDaoTao;
}
