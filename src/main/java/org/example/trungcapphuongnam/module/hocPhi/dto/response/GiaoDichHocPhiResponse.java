package org.example.trungcapphuongnam.module.hocPhi.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.common.enums.PhuongThucGiaoDichHocPhi;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiaoDichHocPhiResponse {
    private Long id;
    private Long hocPhiId;
    private BigDecimal soTien;
    private LocalDateTime ngayGiaoDich;
    private PhuongThucGiaoDichHocPhi phuongThuc;
    private String maGiaoDich;
    private String ghiChu;
    private LocalDateTime createdAt;
}
