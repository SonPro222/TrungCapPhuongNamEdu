package org.example.trungcapphuongnam.module.heThong.dto.response;

import lombok.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaiTroQuyenResponse {
    private Long id;
    private Long vaiTroId;
    private Long quyenId;
}
