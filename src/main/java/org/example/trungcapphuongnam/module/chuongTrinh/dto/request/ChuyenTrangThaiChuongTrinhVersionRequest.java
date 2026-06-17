package org.example.trungcapphuongnam.module.chuongTrinh.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.TrangThaiChuongTrinhVersion;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChuyenTrangThaiChuongTrinhVersionRequest {
    @NotNull(message = "trang_thai không được để trống")
    private TrangThaiChuongTrinhVersion trangThai;
}
