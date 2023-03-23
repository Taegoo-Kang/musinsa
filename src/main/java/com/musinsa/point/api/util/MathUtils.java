package com.musinsa.point.api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class MathUtils {

    public static final BigDecimal HUNDRED = new BigDecimal("100.00");

    public static BigDecimal getPercentage(final long value, final float rate) {
        return getPercentage(value, rate, 0, RoundingMode.DOWN);
    }

    public static BigDecimal getPercentage(final long value, final float rate, final RoundingMode roundingMode) {
        return getPercentage(value, rate, 0, roundingMode);
    }

    public static BigDecimal getPercentage(final long value, final float rate, final int scale, final RoundingMode roundingMode) {
        return BigDecimal.valueOf(value)
            .multiply(BigDecimal.valueOf(rate))
            .divide(HUNDRED, scale, roundingMode);
    }
}
