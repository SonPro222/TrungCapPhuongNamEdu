package org.example.trungcapphuongnam.common.exception;

public class SinhVienNotFoundException extends SinhVienException {
    public SinhVienNotFoundException(String resource, Long id) {
        super(resource + " không tồn tại với id = " + id);
    }
}
