package org.example.trungcapphuongnam.module.lms.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiBaiNopLms;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiNopLmsRequest {
    private Long baiTapLopId;

    private Long sinhVienId;

    private Integer lanNop;

    private String noiDungBaiLam;

    private LocalDateTime thoiGianNop;

    private TrangThaiBaiNopLms trangThai;

    private BigDecimal diem;

    private String nhanXet;

    private Long nguoiChamId;

    private LocalDateTime thoiGianCham;

}
