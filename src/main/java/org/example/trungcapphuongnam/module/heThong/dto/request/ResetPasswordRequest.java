package org.example.trungcapphuongnam.module.heThong.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {

    private Long taiKhoanId;

    private String matKhauMoi;
}