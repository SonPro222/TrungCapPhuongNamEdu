package org.example.trungcapphuongnam.module.heThong.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoanRequest {

    private String email;

    private String matKhau;

    private String loaiTaiKhoan;

    private String trangThai;

    private List<String> roles;
}