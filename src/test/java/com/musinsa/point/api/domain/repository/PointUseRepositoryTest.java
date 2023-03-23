package com.musinsa.point.api.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.musinsa.point.api.config.RepositoryTestConfiguration;
import com.musinsa.point.api.domain.entity.PointUse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@DataJpaTest
class PointUseRepositoryTest {

    @Autowired
    private PointUseRepository pointUseRepository;

    @Test
    void test_findByMemberIdAndOrderNo() {
        var memberId = 10002L;
        var orderNo = 1000010L;

        PointUse pointUse = pointUseRepository.findByMemberIdAndOrderNo(memberId, orderNo);

        assertNotNull(pointUse);
        assertEquals(memberId, pointUse.getMemberId());
        assertEquals(orderNo, pointUse.getOrderNo());
        assertEquals(3000, pointUse.getFreePointAt());
        assertEquals(0, pointUse.getCashPointAt());

        PointUse pointUseNone = pointUseRepository.findByMemberIdAndOrderNo(memberId, 0L);
        assertNull(pointUseNone);
    }
}
