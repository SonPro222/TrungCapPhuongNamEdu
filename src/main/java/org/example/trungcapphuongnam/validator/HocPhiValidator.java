package org.example.trungcapphuongnam.validator;


import org.example.trungcapphuongnam.module.hocPhi.dto.request.GiaoDichHocPhiRequest;
import org.example.trungcapphuongnam.module.hocPhi.dto.request.HocPhiRequest;
import org.example.trungcapphuongnam.module.hocPhi.entity.HocPhi;

import java.math.BigDecimal;

public final class HocPhiValidator {
    private HocPhiValidator() {}

    public static void validateHocPhi(HocPhiRequest request) {
        if (request.getSoTienPhaiDong() == null || request.getSoTienPhaiDong().compareTo(BigDecimal.ZERO) < 0) {
            throw new org.example.trungcapphuongnam.module.hocPhi.exception.HocPhiBusinessException("Số tiền phải đóng không được nhỏ hơn 0");
        }
        if (request.getSoTienDaDong() != null && request.getSoTienDaDong().compareTo(BigDecimal.ZERO) < 0) {
            throw new org.example.trungcapphuongnam.module.hocPhi.exception.HocPhiBusinessException("Số tiền đã đóng không được nhỏ hơn 0");
        }
        if (request.getSoTienDaDong() != null && request.getSoTienDaDong().compareTo(request.getSoTienPhaiDong()) > 0) {
            throw new org.example.trungcapphuongnam.module.hocPhi.exception.HocPhiBusinessException("Số tiền đã đóng không được lớn hơn số tiền phải đóng");
        }
    }

    public static void validateGiaoDich(HocPhi hocPhi, GiaoDichHocPhiRequest request) {
        if (request.getSoTien() == null || request.getSoTien().compareTo(BigDecimal.ZERO) <= 0) {
            throw new org.example.trungcapphuongnam.module.hocPhi.exception.HocPhiBusinessException("Số tiền giao dịch phải lớn hơn 0");
        }
        if (hocPhi == null) {
            throw new org.example.trungcapphuongnam.module.hocPhi.exception.HocPhiBusinessException("Học phí không hợp lệ");
        }
    }
}