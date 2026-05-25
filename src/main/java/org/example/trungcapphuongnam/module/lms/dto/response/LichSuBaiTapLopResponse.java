package org.example.trungcapphuongnam.module.lms.dto.response;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.HanhDongBaiTapLop;
import org.example.trungcapphuongnam.module.lms.enums.VaiTroThucHienLms;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuBaiTapLopResponse {

    private Long id;

    private Long baiTapLopId;

    private Long nguoiThucHienId;

    private VaiTroThucHienLms vaiTroThucHien;

    private HanhDongBaiTapLop hanhDong;

    private JsonNode duLieuCu;

    private JsonNode duLieuMoi;

    private String lyDo;

    private LocalDateTime createdAt;

}
