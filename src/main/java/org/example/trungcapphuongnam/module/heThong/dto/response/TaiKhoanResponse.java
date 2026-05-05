package org.example.trungcapphuongnam.module.heThong.dto.response;

import lombok.*;
import java.time.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TaiKhoanResponse {

    private Long id;
    private String email;
    private String loaiTaiKhoan;
    private String trangThai;

    private List<String> roles;
    private List<String> permissions;
}