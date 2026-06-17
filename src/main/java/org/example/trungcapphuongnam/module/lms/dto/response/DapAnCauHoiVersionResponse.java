package org.example.trungcapphuongnam.module.lms.dto.response;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DapAnCauHoiVersionResponse {
    private Long id;

    private Long cauHoiVersionId;

    private String noiDung;

    private Boolean laDapAnDung;

    private Integer thuTu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
