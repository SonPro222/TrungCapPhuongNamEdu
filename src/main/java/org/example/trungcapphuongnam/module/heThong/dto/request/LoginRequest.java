package org.example.trungcapphuongnam.module.heThong.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    private String email;

    private String matKhau;
}