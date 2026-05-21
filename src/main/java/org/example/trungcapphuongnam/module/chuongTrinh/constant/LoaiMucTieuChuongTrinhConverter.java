package org.example.trungcapphuongnam.module.chuongTrinh.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class LoaiMucTieuChuongTrinhConverter implements AttributeConverter<LoaiMucTieuChuongTrinh, String> {

    @Override
    public String convertToDatabaseColumn(LoaiMucTieuChuongTrinh attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public LoaiMucTieuChuongTrinh convertToEntityAttribute(String dbData) {
        return LoaiMucTieuChuongTrinh.fromValue(dbData);
    }
}
