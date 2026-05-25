package org.example.trungcapphuongnam.module.chuongTrinh.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class LoaiDieuKienMonHocConverter implements AttributeConverter<LoaiDieuKienMonHoc, String> {

    @Override
    public String convertToDatabaseColumn(LoaiDieuKienMonHoc attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public LoaiDieuKienMonHoc convertToEntityAttribute(String dbData) {
        return LoaiDieuKienMonHoc.fromValue(dbData);
    }
}