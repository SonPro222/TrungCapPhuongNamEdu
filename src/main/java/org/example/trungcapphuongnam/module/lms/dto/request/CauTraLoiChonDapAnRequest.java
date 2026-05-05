package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauTraLoiChonDapAnRequest {

    @NotNull

    private Long cauTraLoiId;

    @NotNull

    private Long dapAnId;

}
