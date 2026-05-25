package org.example.trungcapphuongnam.module.chuongTrinh.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum KetQuaDatKhongDat {
    DAT("dat"),
    KHONG_DAT("khong_dat");

    @JsonValue
    private final String value;

    @JsonCreator
    public static KetQuaDatKhongDat fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (KetQuaDatKhongDat item : values()) {
            if (item.value.equalsIgnoreCase(value) || item.name().equalsIgnoreCase(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Giá trị enum không hợp lệ: " + value);
    }
}
