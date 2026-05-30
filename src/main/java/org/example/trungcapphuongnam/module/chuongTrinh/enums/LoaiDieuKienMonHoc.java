package org.example.trungcapphuongnam.module.chuongTrinh.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoaiDieuKienMonHoc {
    PHONG_HOC("phong_hoc"),
    THIET_BI("thiet_bi"),
    HOC_LIEU("hoc_lieu"),
    DUNG_CU("dung_cu"),
    NGUYEN_VAT_LIEU("nguyen_vat_lieu"),
    KHAC("khac");

    @JsonValue
    private final String value;

    @JsonCreator
    public static LoaiDieuKienMonHoc fromValue(String value) {
        if (value == null) {
            return null;
        }

        for (LoaiDieuKienMonHoc item : values()) {
            if (item.value.equalsIgnoreCase(value) || item.name().equalsIgnoreCase(value)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Loại điều kiện môn học không hợp lệ: " + value);
    }
}