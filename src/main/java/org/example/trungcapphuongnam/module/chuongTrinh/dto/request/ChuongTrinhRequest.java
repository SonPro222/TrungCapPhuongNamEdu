package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuongTrinhRequest {

    @NotNull(message = "nganh_id không được để trống")
    private Long nganhId;

    @NotNull(message = "trinh_do_id không được để trống")
    private Long trinhDoId;

    @NotNull(message = "loai_chuong_trinh_id không được để trống")
    private Long loaiChuongTrinhId;

    @NotBlank(message = "ma_chuong_trinh không được để trống")
    private String maChuongTrinh;

    @NotBlank(message = "ten_chuong_trinh không được để trống")
    private String tenChuongTrinh;

    private String doiTuongTuyenSinh;

    private String thoiGianDaoTao;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
