package org.example.trungcapphuongnam.module.donTu;

public class DonTuNotFoundException extends DonTuException {
    public DonTuNotFoundException(String resource, Long id) {
        super(resource + " không tồn tại với id = " + id);
    }
}
