package org.example.trungcapphuongnam.module.chuongTrinh.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class LoaiNangLucDauRaConverter implements AttributeConverter<LoaiNangLucDauRa, String> {

    @Override
    public String convertToDatabaseColumn(LoaiNangLucDauRa attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public LoaiNangLucDauRa convertToEntityAttribute(String dbData) {
        return LoaiNangLucDauRa.fromValue(dbData);
    }
}
