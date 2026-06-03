package org.example.trungcapphuongnam.module.lms.dto.response;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiNopLmsTepResponse {
    private Long id;

    private Long baiNopLmsId;

    private Long tepDinhKemId;

    private LocalDateTime createdAt;

}
