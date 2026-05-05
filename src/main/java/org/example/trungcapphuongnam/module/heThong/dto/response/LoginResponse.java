package org.example.trungcapphuongnam.module.heThong.dto.response;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String accessToken;

    private String tokenType;

    private Long taiKhoanId;

    private String email;

    private String loaiTaiKhoan;

    private String trangThai;

    private List<String> roles;

    private List<String> permissions;
}