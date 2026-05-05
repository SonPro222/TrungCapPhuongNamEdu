package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DapAnCauHoiRequest {

    @NotNull

    private Long cauHoiId;

    @NotBlank

    private String noiDung;

    private Boolean laDapAnDung;

    private Integer thuTu;

}
