package org.example.trungcapphuongnam.module.hocPhi;

public class HocPhiNotFoundException extends RuntimeException {
    public HocPhiNotFoundException(Long id) {
        super("Không tìm thấy học phí với id: " + id);
    }
}
