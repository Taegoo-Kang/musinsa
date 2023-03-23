package com.musinsa.point.api.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.musinsa.point.api.config.RepositoryTestConfiguration;
import com.musinsa.point.api.domain.entity.Point;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@DataJpaTest
class PointRepositoryTest {

    @Autowired
    private PointRepository pointRepository;

    @Test
    void test_findUsablePoints() {
        var memberId = 10001L;

        List<Point> usablePoints = pointRepository.findUsablePoints(memberId);

        assertEquals(7, usablePoints.size());
        assertEquals(11L, usablePoints.get(0).getPointNo());
        assertEquals(5L, usablePoints.get(2).getOriginPointNo());
        assertEquals(1731L, usablePoints.get(2).getRemainAt());
        assertEquals(3561L, usablePoints.get(4).getAccumulateAt());
        assertEquals(LocalDate.of(2027,12, 31), usablePoints.get(6).getExpireDate());

        assertTrue(usablePoints.stream()
            .noneMatch(point -> point.getPointNo().equals(12L))
        );
    }
}
