package org.example.trungcapphuongnam.module.hocPhi.dto.response;

import lombok.*;
import org.example.trungcapphuongnam.common.enums.TrangThaiHocPhi;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HocPhiResponse {
    private Long id;
    private Long sinhVienChuongTrinhId;
    private String tenKhoanThu;
    private BigDecimal soTienPhaiDong;
    private BigDecimal soTienDaDong;
    private BigDecimal soTienConLai;
    private LocalDate hanDong;
    private LocalDate ngayDong;
    private TrangThaiHocPhi trangThai;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
