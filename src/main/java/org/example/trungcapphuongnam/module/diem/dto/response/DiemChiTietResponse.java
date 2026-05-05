package org.example.trungcapphuongnam.module.diem.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiemChiTietResponse {
    private Long id;

    private Long sinhVienId;

    private Long lopHocPhanId;

    private Long cauHinhDanhGiaId;

    private Long baiKiemTraId;

    private BigDecimal diem;

    private String nhanXet;

    private Long giaoVienChamId;

    private LocalDateTime ngayCham;

    private String trangThai;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
