package org.example.trungcapphuongnam.module.lms.dto.response.nghiepvu;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KetQuaChamTuDong {
    private BigDecimal diemTuDong;
    private int soCauDung;
    private int soCauSai;
    private int tongSoCau;
    private boolean canGiaoVienCham;
}
