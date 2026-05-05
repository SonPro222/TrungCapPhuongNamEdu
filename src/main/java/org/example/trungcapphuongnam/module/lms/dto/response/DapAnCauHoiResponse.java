package org.example.trungcapphuongnam.module.lms.dto.response;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DapAnCauHoiResponse {

    private Long id;

    private Long cauHoiId;

    private String noiDung;

    private Boolean laDapAnDung;

    private Integer thuTu;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
