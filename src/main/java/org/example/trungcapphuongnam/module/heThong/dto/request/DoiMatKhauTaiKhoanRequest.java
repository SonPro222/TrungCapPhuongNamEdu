package org.example.trungcapphuongnam.module.heThong.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoiMatKhauTaiKhoanRequest {

    private String matKhauMoi;

    private String xacNhanMatKhau;
}
