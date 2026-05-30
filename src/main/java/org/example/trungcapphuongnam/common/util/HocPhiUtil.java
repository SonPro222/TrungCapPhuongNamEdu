package org.example.trungcapphuongnam.common.util;

import org.example.trungcapphuongnam.module.hocPhi.enums.TrangThaiHocPhi;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class HocPhiUtil {
    private HocPhiUtil() {}

    public static TrangThaiHocPhi tinhTrangThai(BigDecimal soTienPhaiDong, BigDecimal soTienDaDong, LocalDate hanDong) {
        BigDecimal phaiDong = soTienPhaiDong == null ? BigDecimal.ZERO : soTienPhaiDong;
        BigDecimal daDong = soTienDaDong == null ? BigDecimal.ZERO : soTienDaDong;

        if (phaiDong.compareTo(BigDecimal.ZERO) == 0 || daDong.compareTo(phaiDong) >= 0) {
            return TrangThaiHocPhi.da_dong;
        }
        if (hanDong != null && LocalDate.now().isAfter(hanDong)) {
            return TrangThaiHocPhi.qua_han;
        }
        if (daDong.compareTo(BigDecimal.ZERO) > 0) {
            return TrangThaiHocPhi.dong_mot_phan;
        }
        return TrangThaiHocPhi.chua_dong;
    }

    public static BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
