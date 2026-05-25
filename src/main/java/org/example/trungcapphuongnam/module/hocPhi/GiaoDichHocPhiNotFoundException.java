package org.example.trungcapphuongnam.module.hocPhi.exception;

public class GiaoDichHocPhiNotFoundException extends RuntimeException {
    public GiaoDichHocPhiNotFoundException(Long id) {
        super("Không tìm thấy giao dịch học phí với id: " + id);
    }
}
