package org.example.trungcapphuongnam.module.lms.dto.response;

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
    private Long lanLamBaiDapAnId;
    private LocalDateTime createdAt;
}
