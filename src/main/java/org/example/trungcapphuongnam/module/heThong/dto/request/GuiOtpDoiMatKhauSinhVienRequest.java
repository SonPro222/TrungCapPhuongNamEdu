package org.example.trungcapphuongnam.module.heThong.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuiOtpDoiMatKhauSinhVienRequest {
    private String email;
}
