package org.example.trungcapphuongnam.module.lms.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhatKyLamBaiOnlineResponse {
    private Long id;

    private Long lanLamBaiId;

    private String hanhDong;

    private JsonNode duLieu;

    private String ipAddress;

    private String userAgent;

    private LocalDateTime createdAt;

}
