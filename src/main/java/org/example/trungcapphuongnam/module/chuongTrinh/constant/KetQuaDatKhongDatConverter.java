package org.example.trungcapphuongnam.module.chuongTrinh.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class KetQuaDatKhongDatConverter implements AttributeConverter<KetQuaDatKhongDat, String> {

    @Override
    public String convertToDatabaseColumn(KetQuaDatKhongDat attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public KetQuaDatKhongDat convertToEntityAttribute(String dbData) {
        return KetQuaDatKhongDat.fromValue(dbData);
    }
}
