package org.example.trungcapphuongnam.module.heThong.dto.response;

import lombok.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoanVaiTroResponse {
    private Long id;
    private Long taiKhoanId;
    private Long vaiTroId;
}
