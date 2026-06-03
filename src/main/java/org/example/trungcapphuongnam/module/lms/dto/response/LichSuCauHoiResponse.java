package org.example.trungcapphuongnam.module.lms.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuCauHoiResponse {
    private Long id;

    private Long cauHoiGocId;

    private Long cauHoiVersionId;

    private String hanhDong;

    private JsonNode noiDungCu;

    private JsonNode noiDungMoi;

    private Long nguoiThucHienId;

    private LocalDateTime thoiGian;

    private String ghiChu;

}
