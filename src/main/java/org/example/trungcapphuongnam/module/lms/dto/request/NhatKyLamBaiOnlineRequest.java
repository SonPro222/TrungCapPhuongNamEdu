package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NhatKyLamBaiOnlineRequest {
    private Long lanLamBaiId;

    private String hanhDong;

    private JsonNode duLieu;

    private String ipAddress;

    private String userAgent;

}
