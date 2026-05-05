package org.example.trungcapphuongnam.module.lms.dto.response;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CauTraLoiChonDapAnResponse {

    private Long id;

    private Long cauTraLoiId;

    private Long dapAnId;

    private LocalDateTime createdAt;

}
