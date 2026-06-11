package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhRequest {

    /** Mô hình mới: chọn ngành hệ đào tạo thay vì chọn rời trinhDo + loaiChuongTrinh */
    private Long nganhHeDaoTaoId;

    @NotBlank(message = "ma_chuong_trinh không được để trống")
    private String maChuongTrinh;

    @NotBlank(message = "ten_chuong_trinh không được để trống")
    private String tenChuongTrinh;

    private String doiTuongTuyenSinh;

    private String thoiGianDaoTao;
}
