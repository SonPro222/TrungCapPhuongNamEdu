package org.example.trungcapphuongnam.module.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiBaiNopLms;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiNopLmsResponse {
    private Long id;

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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
