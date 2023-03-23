package com.musinsa.point.api.converter;

import com.musinsa.point.api.model.type.PointStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PointStatusConverter implements AttributeConverter<PointStatus, String> {

    @Override
    public String convertToDatabaseColumn(PointStatus attribute) {
        return attribute.getCode();
    }

    @Override
    public PointStatus convertToEntityAttribute(String dbData) {
        return PointStatus.of(dbData);
    }
}
