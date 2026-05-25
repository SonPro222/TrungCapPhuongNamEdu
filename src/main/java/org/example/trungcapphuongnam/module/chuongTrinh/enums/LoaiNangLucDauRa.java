package org.example.trungcapphuongnam.module.chuongTrinh.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoaiNangLucDauRa {
    CO_BAN("co_ban"),
    COT_LOI("cot_loi"),
    NANG_CAO("nang_cao");

    @JsonValue
    private final String value;

    @JsonCreator
    public static LoaiNangLucDauRa fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (LoaiNangLucDauRa item : values()) {
            if (item.value.equalsIgnoreCase(value) || item.name().equalsIgnoreCase(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Giá trị enum không hợp lệ: " + value);
    }
}
