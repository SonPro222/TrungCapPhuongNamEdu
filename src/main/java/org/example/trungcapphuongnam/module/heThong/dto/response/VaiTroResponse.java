package org.example.trungcapphuongnam.module.heThong.dto.response;

import lombok.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VaiTroResponse {
    private Long id;
    private String maVaiTro;
    private String tenVaiTro;
    private String moTa;
}
