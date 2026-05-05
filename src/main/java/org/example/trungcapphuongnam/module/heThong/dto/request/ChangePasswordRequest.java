package org.example.trungcapphuongnam.module.heThong.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {

    private String matKhauCu;

    private String matKhauMoi;

    private String xacNhanMatKhauMoi;
}