package org.example.trungcapphuongnam.module.lms.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.LoaiBaiOnline;
import org.example.trungcapphuongnam.module.lms.enums.NguonTao;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiBaiTapOnline;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaiTapOnlineResponse {

    private Long id;

    private Long chuongTrinhMonId;

    private Long nguoiTaoTaiKhoanId;

    private NguonTao nguonTao;

    private String tieuDe;

    private String moTa;

    private LoaiBaiOnline loaiBai;

    private BigDecimal tongDiem;

    private TrangThaiBaiTapOnline trangThai;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
