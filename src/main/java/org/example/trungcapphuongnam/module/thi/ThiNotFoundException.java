package org.example.trungcapphuongnam.module.thi;

public class ThiNotFoundException extends ThiException {
    public ThiNotFoundException(String resource, Long id) {
        super(resource + " không tồn tại với id = " + id);
    }
}
