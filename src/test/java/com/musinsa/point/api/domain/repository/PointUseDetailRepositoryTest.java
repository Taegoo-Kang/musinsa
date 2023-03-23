package com.musinsa.point.api.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.musinsa.point.api.config.RepositoryTestConfiguration;
import com.musinsa.point.api.domain.entity.Point;
import com.musinsa.point.api.domain.entity.PointUseDetail;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@DataJpaTest
class PointUseDetailRepositoryTest {

    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private PointUseDetailRepository pointUseDetailRepository;

    @Test
    void test_savePointUseDetail() {
        final var point = pointRepository.save(Point.builder()
            .pointType("TEST")
            .memberId(1L)
            .description("TEST PointType")
            .accumulateAt(100L)
            .remainAt(100L)
            .isCash(false)
            .expireDate(LocalDate.now())
            .build());

        final var pointUseDetail = pointUseDetailRepository.save(PointUseDetail.builder()
            .pointUseNo(10L)
            .point(point)
            .pointAt(point.getRemainAt())
            .build());

        assertNotNull(pointUseDetail);
    }

    @Test
    void test_findCancelableDetails() {
        var useNo = 2L;

        List<PointUseDetail> useDetails = pointUseDetailRepository.findCancelableDetails(useNo);

        assertEquals(1, useDetails.size());
        assertEquals(6L, useDetails.get(0).getPoint().getPointNo());
        assertEquals(3000L, useDetails.get(0).getPointAt());
        assertEquals(1000L, useDetails.get(0).getCancelPointAt());
    }
}
