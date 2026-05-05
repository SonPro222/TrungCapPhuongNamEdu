package org.example.trungcapphuongnam.module.heThong.dto.request;

import lombok.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuyenRequest {
    private String maQuyen;
    private String tenQuyen;
    private String moTa;
}
