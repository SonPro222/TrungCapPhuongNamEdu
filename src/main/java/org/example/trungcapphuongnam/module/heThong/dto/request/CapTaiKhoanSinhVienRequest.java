package org.example.trungcapphuongnam.module.heThong.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CapTaiKhoanSinhVienRequest {

    @NotBlank(message = "Email cấp tài khoản không được để trống")
    @Email(message = "Email cấp tài khoản không đúng định dạng")
    private String email;

    private String trangThai;
}