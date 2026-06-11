package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuCauHoiRequest {
    private Long cauHoiMauId;

    private Long cauHoiVersionId;

    private String hanhDong;

    private JsonNode noiDungCu;

    private JsonNode noiDungMoi;

    private Long nguoiThucHienId;

    private String ghiChu;

}
