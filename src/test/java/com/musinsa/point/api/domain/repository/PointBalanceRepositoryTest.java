package com.musinsa.point.api.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.musinsa.point.api.config.RepositoryTestConfiguration;
import com.musinsa.point.api.domain.entity.PointBalance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryTestConfiguration.class)
@DataJpaTest
class PointBalanceRepositoryTest {

    @Autowired
    private PointBalanceRepository pointBalanceRepository;

    @DisplayName("회원 포인트 잔액 증/차감")
    @Test
    void test_findByMemberId() {
        var memberId = 10001L;

        PointBalance pointBalance = pointBalanceRepository.findByMemberId(memberId);

        assertNotNull(pointBalance);
        assertEquals(memberId, pointBalance.getMemberId());
        assertEquals(7038L, pointBalance.getFreePoint());
        assertEquals(10000L, pointBalance.getCashPoint());
        assertEquals(17038L, pointBalance.getTotalPoint());

        pointBalance.addFreePoint(1000L);
        pointBalance.addFreePoint(-2000L);
        pointBalance.addCashPoint(10L);
        pointBalance.addCashPoint(-10000L);

        assertEquals(6038L, pointBalance.getFreePoint());
        assertEquals(10L, pointBalance.getCashPoint());
        assertEquals(6048L, pointBalance.getTotalPoint());
    }

    @DisplayName("회원 포인트 잔액 조회")
    @Test
    void test_findTotalPoint() {
        var memberId = 10001L;

        Long totalPoint = pointBalanceRepository.findTotalPointByMemberId(memberId);

        assertEquals(17038L, totalPoint);
    }
}
