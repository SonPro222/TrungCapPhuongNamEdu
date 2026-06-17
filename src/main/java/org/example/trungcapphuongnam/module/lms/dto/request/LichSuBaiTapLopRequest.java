package org.example.trungcapphuongnam.module.lms.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.*;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.HanhDongBaiTapLop;
import org.example.trungcapphuongnam.module.lms.enums.VaiTroThucHienLms;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LichSuBaiTapLopRequest {

    @NotNull

    private Long baiTapLopId;

    @NotNull

    private Long nguoiThucHienId;

    private VaiTroThucHienLms vaiTroThucHien;

    private HanhDongBaiTapLop hanhDong;

    private JsonNode duLieuCu;

    private JsonNode duLieuMoi;


    private String lyDo;

}
