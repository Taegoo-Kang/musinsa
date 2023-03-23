package com.musinsa.point.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musinsa.point.api.domain.entity.PointBalance;
import com.musinsa.point.api.domain.repository.PointBalanceRepository;
import com.musinsa.point.api.model.dto.PointBalanceDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointBalanceServiceTest {

    @Mock
    private PointBalanceRepository pointBalanceRepository;

    @InjectMocks
    private PointBalanceService pointBalanceService;

    @DisplayName("비회원 잔여 포인트 조회")
    @Test
    void test_getPointBalance_none() {
        final var memberId = 12345L;

        when(pointBalanceRepository.findByMemberId(memberId)).thenReturn(null);

        final var balance = pointBalanceService.getPointBalance(memberId);
        assertEquals(memberId, balance.memberId());
        assertEquals(0L, balance.totalPoint());
    }

    @DisplayName("잔여 포인트 조회")
    @Test
    void test_getPointBalance() {
        final var expected = PointBalanceDto.builder()
            .memberId(12345L)
            .freePoint(130000L)
            .cashPoint(5000L)
            .build();

        when(pointBalanceRepository.findByMemberId(expected.memberId())).thenReturn(PointBalance.builder()
            .memberId(expected.memberId())
            .freePoint(expected.freePoint())
            .cashPoint(expected.cashPoint())
            .build());

        final var balance = pointBalanceService.getPointBalance(expected.memberId());
        assertEquals(expected.memberId(), balance.memberId());
        assertEquals(expected.freePoint(), balance.freePoint());
        assertEquals(expected.cashPoint(), balance.cashPoint());
        assertEquals(expected.freePoint() + expected.cashPoint(), balance.totalPoint());
    }

    @DisplayName("신규 적립 잔여 포인트 증가")
    @Test
    void test_plusPointBalance_none() {
        final var memberId = 12345L;
        final var freePoint = 2000L;
        final var cashPoint = 10000L;

        when(pointBalanceRepository.findByMemberId(memberId)).thenReturn(null);

        final var balance = pointBalanceService.plusPointBalance(memberId, freePoint, cashPoint);
        assertEquals(memberId, balance.memberId());
        assertEquals(freePoint, balance.freePoint());
        assertEquals(cashPoint, balance.cashPoint());
        assertEquals(freePoint + cashPoint  , balance.totalPoint());

        verify(pointBalanceRepository, times(1)).save(any());
    }

    @DisplayName("잔여 포인트 증가")
    @Test
    void test_plusPointBalance() {
        final var memberId = 12345L;
        final var freePoint = 2000L;
        final var cashPoint = 10000L;
        final var memberFreePoint = 9378L;
        final var memberCashPoint = 100L;

        final var memberPoint = PointBalance.builder()
            .memberId(memberId)
            .freePoint(memberFreePoint)
            .cashPoint(memberCashPoint)
            .build();

        when(pointBalanceRepository.findByMemberId(memberId)).thenReturn(memberPoint);

        final var balance = pointBalanceService.plusPointBalance(memberId, freePoint, cashPoint);
        assertEquals(memberId, balance.memberId());
        assertEquals(freePoint + memberFreePoint, balance.freePoint());
        assertEquals(cashPoint + memberCashPoint, balance.cashPoint());
        assertEquals(freePoint + memberFreePoint + cashPoint + memberCashPoint, balance.totalPoint());

        verify(pointBalanceRepository, times(0)).save(any());
    }

    @DisplayName("잔여 포인트 차감")
    @Test
    void test_minusPointBalance() {
        final var memberId = 12345L;
        final var freePoint = 2000L;
        final var cashPoint = 10000L;
        final var memberFreePoint = 9378L;
        final var memberCashPoint = 23000L;

        final var memberPoint = PointBalance.builder()
            .memberId(memberId)
            .freePoint(memberFreePoint)
            .cashPoint(memberCashPoint)
            .build();

        when(pointBalanceRepository.findByMemberId(memberId)).thenReturn(memberPoint);

        final var balance = pointBalanceService.minusPointBalance(memberId, freePoint, cashPoint);
        assertEquals(memberId, balance.memberId());
        assertEquals(memberFreePoint - freePoint, balance.freePoint());
        assertEquals(memberCashPoint - cashPoint, balance.cashPoint());
        assertEquals((memberFreePoint - freePoint) + (memberCashPoint - cashPoint), balance.totalPoint());
    }
}
