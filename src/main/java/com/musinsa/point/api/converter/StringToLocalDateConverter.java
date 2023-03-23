package com.musinsa.point.api.converter;

import com.musinsa.point.api.util.DateTimeUtils;
import java.time.LocalDate;
import org.springframework.core.convert.converter.Converter;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        final var date = source.replaceAll("[^0-9]", "");
        return LocalDate.parse(date, DateTimeUtils.DEFAULT_DATE_FORMAT);
    }
}
