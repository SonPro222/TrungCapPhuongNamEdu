package org.example.trungcapphuongnam.module.heThong.dto.response;

import lombok.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuyenResponse {
    private Long id;
    private String maQuyen;
    private String tenQuyen;
    private String moTa;
}
