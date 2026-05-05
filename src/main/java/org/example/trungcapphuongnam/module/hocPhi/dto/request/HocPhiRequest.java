package org.example.trungcapphuongnam.module.hocPhi.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.example.trungcapphuongnam.common.enums.TrangThaiHocPhi;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HocPhiRequest {

    @NotNull(message = "Sinh viên chương trình id không được để trống")
    private Long sinhVienChuongTrinhId;

    private String tenKhoanThu;

    @NotNull(message = "Số tiền phải đóng không được để trống")
    @DecimalMin(value = "0.0", message = "Số tiền phải đóng không được nhỏ hơn 0")
    private BigDecimal soTienPhaiDong;

    @DecimalMin(value = "0.0", message = "Số tiền đã đóng không được nhỏ hơn 0")
    private BigDecimal soTienDaDong;

    private LocalDate hanDong;
    private LocalDate ngayDong;
    private TrangThaiHocPhi trangThai;
    private String ghiChu;
}
