package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiNopLmsTepRequest {
    private Long baiNopLmsId;

    private Long tepDinhKemId;

}
