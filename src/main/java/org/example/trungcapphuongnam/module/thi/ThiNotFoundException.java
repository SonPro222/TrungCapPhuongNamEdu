package org.example.trungcapphuongnam.common.exception;

public class ThiNotFoundException extends ThiException {
    public ThiNotFoundException(String resource, Long id) {
        super(resource + " không tồn tại với id = " + id);
    }
}
