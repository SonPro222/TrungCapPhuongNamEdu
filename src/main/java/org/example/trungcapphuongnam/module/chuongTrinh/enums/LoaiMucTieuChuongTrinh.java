package org.example.trungcapphuongnam.module.chuongTrinh.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoaiMucTieuChuongTrinh {
    CHUNG("chung"),
    KIEN_THUC("kien_thuc"),
    KY_NANG("ky_nang"),
    NANG_LUC_TU_CHU_TRACH_NHIEM("nang_luc_tu_chu_trach_nhiem");

    @JsonValue
    private final String value;

    @JsonCreator
    public static LoaiMucTieuChuongTrinh fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (LoaiMucTieuChuongTrinh item : values()) {
            if (item.value.equalsIgnoreCase(value) || item.name().equalsIgnoreCase(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Giá trị enum không hợp lệ: " + value);
    }
}
