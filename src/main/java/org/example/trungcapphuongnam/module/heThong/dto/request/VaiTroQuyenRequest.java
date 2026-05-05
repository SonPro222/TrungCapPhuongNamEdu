package org.example.trungcapphuongnam.module.heThong.dto.request;

import lombok.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaiTroQuyenRequest {
    private Long vaiTroId;
    private Long quyenId;
}
