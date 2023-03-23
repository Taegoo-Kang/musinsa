package com.musinsa.point.api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MathUtilsTest {

    @Test
    void test_percentage_down() {
        final var value = 9999L;
        final var rate = 1.3f;

        final var integer = MathUtils.getPercentage(value, rate);
        final var integerHalfDown = MathUtils.getPercentage(value, rate, RoundingMode.HALF_DOWN);
        final var floatHalfDown = MathUtils.getPercentage(value, rate, 2, RoundingMode.HALF_DOWN);

        Assertions.assertEquals(BigDecimal.valueOf(129L), integer);
        Assertions.assertEquals(BigDecimal.valueOf(130L), integerHalfDown);
        Assertions.assertEquals(new BigDecimal("129.99"), floatHalfDown);
    }
}
