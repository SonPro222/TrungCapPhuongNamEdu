package org.example.trungcapphuongnam.module.heThong.dto.request;

import lombok.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaiKhoanVaiTroRequest {
    private Long taiKhoanId;
    private Long vaiTroId;
}
