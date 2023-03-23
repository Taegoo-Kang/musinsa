package com.musinsa.point.api.domain.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.musinsa.point.api.config.RepositoryTestConfiguration;
import com.musinsa.point.api.domain.entity.PointType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@DataJpaTest
class PointTypeRepositoryTest {

    @Autowired
    private PointTypeRepository pointTypeRepository;

    @Test
    void test_findEnabledByPointType() {

        PointType pointType = pointTypeRepository.findEnabledByPointType("PT_0009");

        assertNotNull(pointType);
        assertEquals("오늘만 사용", pointType.getPointTypeName());
        assertEquals(100.0f, pointType.getRate());
        assertFalse(pointType.getIsCash());
        assertEquals(0, pointType.getExpirePeriod());
        assertEquals("Y", pointType.getUseYn());
    }

    @Test
    void test_findEnabledByPointType_empty() {

        PointType pointType = pointTypeRepository.findEnabledByPointType("PT_0003");

        assertNull(pointType);
    }
}
