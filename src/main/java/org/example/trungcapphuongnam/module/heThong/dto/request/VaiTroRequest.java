package org.example.trungcapphuongnam.module.heThong.dto.request;

import lombok.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaiTroRequest {
    private String maVaiTro;
    private String tenVaiTro;
    private String moTa;
}
