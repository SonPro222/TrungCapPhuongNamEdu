package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DapAnCauHoiVersionRequest {
    private Long cauHoiVersionId;

    private String noiDung;

    private Boolean laDapAnDung;

    private Integer thuTu;

}
