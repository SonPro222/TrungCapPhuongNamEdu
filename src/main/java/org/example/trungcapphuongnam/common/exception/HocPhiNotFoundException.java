package org.example.trungcapphuongnam.module.hocPhi.exception;

public class HocPhiNotFoundException extends RuntimeException {
    public HocPhiNotFoundException(Long id) {
        super("Không tìm thấy học phí với id: " + id);
    }
}
