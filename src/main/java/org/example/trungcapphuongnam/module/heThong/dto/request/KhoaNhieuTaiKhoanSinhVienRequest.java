package org.example.trungcapphuongnam.module.heThong.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KhoaNhieuTaiKhoanSinhVienRequest {

    @NotEmpty(message = "Danh sách sinh viên không được để trống")
    private List<Long> sinhVienIds;
}