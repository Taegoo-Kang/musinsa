package com.musinsa.point.api.util;

import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class DateTimeUtils {

    public static final DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter POINT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd");
}
