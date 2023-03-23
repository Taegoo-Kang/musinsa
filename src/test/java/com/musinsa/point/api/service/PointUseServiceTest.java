package com.musinsa.point.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musinsa.point.api.domain.entity.Point;
import com.musinsa.point.api.domain.entity.PointUse;
import com.musinsa.point.api.domain.entity.PointUseDetail;
import com.musinsa.point.api.domain.repository.PointBalanceRepository;
import com.musinsa.point.api.domain.repository.PointHistoryRepository;
import com.musinsa.point.api.domain.repository.PointRepository;
import com.musinsa.point.api.domain.repository.PointUseDetailRepository;
import com.musinsa.point.api.domain.repository.PointUseRepository;
import com.musinsa.point.api.exception.PointApiException;
import com.musinsa.point.api.model.dto.PointBalanceDto;
import com.musinsa.point.api.model.dto.PointUseParam;
import com.musinsa.point.api.model.type.ErrorType;
import com.musinsa.point.api.model.type.PointTypes;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointUseServiceTest {

    @Mock
    private PointBalanceRepository pointBalanceRepository;
    @Mock
    private PointRepository pointRepository;
    @Mock
    private PointUseRepository pointUseRepository;
    @Mock
    private PointUseDetailRepository pointUseDetailRepository;
    @Mock
    private PointHistoryRepository pointHistoryRepository;
    @Mock
    private PointBalanceService pointBalanceService;

    @InjectMocks
    private PointUseService pointUseService;

    @DisplayName("0포인트 사용")
    @Test
    void test_useZeroPoint() {
        final var param = PointUseParam.builder()
            .memberId(12345L)
            .orderNo(20230301L)
            .useAt(0)
            .build();

        final PointBalanceDto balance = pointUseService.usePoint(param);

        assertNull(balance);
    }

    @DisplayName("잔여 포인트 이상의 금액 사용")
    @Test
    void test_overEnablePoint() {
        final var param = PointUseParam.builder()
            .memberId(12345L)
            .orderNo(20230301L)
            .useAt(10000L)
            .build();

        when(pointBalanceRepository.findTotalPointByMemberId(param.memberId())).thenReturn(9999L);

        final var exception = assertThrows(PointApiException.class, () -> pointUseService.usePoint(param));
        assertEquals(ErrorType.OVER_ENABLE_POINT, exception.getErrorType());
    }

    @DisplayName("만료된 포인트 사용")
    @Test
    void test_useExpiredPoint() {
        final var memberId = 12345L;
        final var param = PointUseParam.builder()
            .memberId(memberId)
            .orderNo(20230301L)
            .useAt(10000L)
            .build();

        final var point1 = Point.builder()
            .pointNo(1L)
            .memberId(memberId)
            .pointType(PointTypes.ORDER_CONFIRM.getPointType())
            .remainAt(2400L)
            .isCash(false)
            .build();

        final var point2 = Point.builder()
            .pointNo(1L)
            .memberId(memberId)
            .pointType("CASH_POINT")
            .remainAt(3500L)
            .isCash(true)
            .build();

        final var point3 = Point.builder()
            .pointNo(1L)
            .memberId(memberId)
            .pointType("FREE_POINT!")
            .remainAt(4010L)
            .isCash(false)
            .build();

        when(pointBalanceRepository.findTotalPointByMemberId(memberId)).thenReturn(10000L);
        when(pointRepository.findUsablePoints(memberId)).thenReturn(List.of(point1, point2, point3));

        final var exception = assertThrows(PointApiException.class, () -> pointUseService.usePoint(param));
        assertEquals(ErrorType.USE_EXPIRED_POINT, exception.getErrorType());
    }

    @DisplayName("포인트 사용")
    @Test
    void test_usePoint() {
        final var memberId = 12345L;
        final var param = PointUseParam.builder()
            .memberId(memberId)
            .orderNo(20230301L)
            .useAt(10000L)
            .build();

        final var point1 = Point.builder()
            .pointNo(1L)
            .memberId(memberId)
            .pointType(PointTypes.ORDER_CONFIRM.getPointType())
            .remainAt(2400L)
            .isCash(false)
            .build();

        final var point2 = Point.builder()
            .pointNo(2L)
            .memberId(memberId)
            .pointType("CASH_POINT")
            .remainAt(3500L)
            .isCash(true)
            .build();

        final var point3 = Point.builder()
            .pointNo(3L)
            .memberId(memberId)
            .pointType("FREE_POINT!")
            .remainAt(5000L)
            .isCash(false)
            .build();

        final var totalPoint = point1.getRemainAt() + point2.getRemainAt() + point3.getRemainAt();
        when(pointBalanceRepository.findTotalPointByMemberId(memberId)).thenReturn(totalPoint);
        when(pointRepository.findUsablePoints(memberId)).thenReturn(List.of(point1, point2, point3));

        final var point1UsedAt = point1.getRemainAt();
        final var point2UsedAt = point2.getRemainAt();
        final var point3UsedAt = param.useAt() - point1UsedAt - point2UsedAt;
        final var useFreePointAt = point1UsedAt + point3UsedAt;
        final var useCashPointAt = point2UsedAt;
        final var pointUse = PointUse.builder()
            .pointUseNo(1L)
            .memberId(memberId)
            .orderNo(param.orderNo())
            .freePointAt(useFreePointAt)
            .cashPointAt(useCashPointAt)
            .build();
        when(pointUseRepository.save(any())).thenReturn(pointUse);

        final var pointUseDetail = PointUseDetail.builder()
            .seq(1L)
            .point(point1)
            .pointUseNo(pointUse.getPointUseNo())
            .pointAt(point1UsedAt)
            .build();

        when(pointUseDetailRepository.save(any())).thenReturn(pointUseDetail);

        pointUseService.usePoint(param);

        verify(pointUseDetailRepository, times(3)).save(any());
        verify(pointHistoryRepository, times(3)).save(any());
        verify(pointBalanceService, times(1)).minusPointBalance(memberId, useFreePointAt, useCashPointAt);
    }

    @DisplayName("포인트 사용 취소 유효성 검증")
    @Test
    void test_notFoundPointUse() {
        final var param = PointUseParam.builder()
            .memberId(12345L)
            .orderNo(10L)
            .useAt(10000L)
            .build();

        when(pointUseRepository.findByMemberIdAndOrderNo(param.memberId(), param.orderNo())).thenReturn(null);

        final var exception = assertThrows(PointApiException.class, () -> pointUseService.cancelUsedPoint(param));
        assertEquals(ErrorType.NOT_FOUND_USED_POINT, exception.getErrorType());
    }

    @DisplayName("포인트 사용 취소 금액 검증")
    @Test
    void test_overCancelUsedPoint() {
        final var param = PointUseParam.builder()
            .memberId(12345L)
            .orderNo(10L)
            .useAt(10000L)
            .build();

        final var pointuse = PointUse.builder()
            .pointUseNo(1L)
            .memberId(param.memberId())
            .freePointAt(7000L)
            .cashPointAt(1000L)
            .build();

        when(pointUseRepository.findByMemberIdAndOrderNo(param.memberId(), param.orderNo())).thenReturn(pointuse);

        final var exception = assertThrows(PointApiException.class, () -> pointUseService.cancelUsedPoint(param));
        assertEquals(ErrorType.OVER_CANCEL_USED_POINT, exception.getErrorType());
    }

    @DisplayName("포인트 사용 취소 실패")
    @Test
    void test_failCancelPoint() {
        final var param = PointUseParam.builder()
            .memberId(12345L)
            .orderNo(10L)
            .useAt(10000L)
            .build();

        final var pointuse = PointUse.builder()
            .pointUseNo(1L)
            .memberId(param.memberId())
            .freePointAt(7000L)
            .cashPointAt(5000L)
            .build();
        when(pointUseRepository.findByMemberIdAndOrderNo(param.memberId(), param.orderNo())).thenReturn(pointuse);

        final var point1 = Point.builder()
            .pointNo(1L)
            .memberId(param.memberId())
            .pointType(PointTypes.ORDER_CONFIRM.getPointType())
            .remainAt(0L)
            .isCash(false)
            .build();

        final var point2 = Point.builder()
            .pointNo(2L)
            .memberId(param.memberId())
            .pointType("CASH_POINT")
            .remainAt(3500L)
            .isCash(true)
            .build();

        final var pointUseDetail1 = PointUseDetail.builder()
            .pointUseNo(pointuse.getPointUseNo())
            .point(point1)
            .pointAt(7000L)
            .build();

        final var pointUseDetail2 = PointUseDetail.builder()
            .pointUseNo(pointuse.getPointUseNo())
            .point(point2)
            .pointAt(2500L)
            .build();

        final var pointUseDetailList = List.of(pointUseDetail1, pointUseDetail2);
        when(pointUseDetailRepository.findCancelableDetails(pointuse.getPointUseNo())).thenReturn(pointUseDetailList);

        when(pointRepository.save(any())).thenReturn(point1);

        final var exception = assertThrows(PointApiException.class, () -> pointUseService.cancelUsedPoint(param));
        assertEquals(ErrorType.FAIL_CANCEL_USED_POINT, exception.getErrorType());
    }

    @DisplayName("포인트 원 정보 조회 실패")
    @Test
    void test_failGetOriginPoint() {
        final var param = PointUseParam.builder()
            .memberId(12345L)
            .orderNo(10L)
            .useAt(10000L)
            .build();

        final var pointuse = PointUse.builder()
            .pointUseNo(1L)
            .memberId(param.memberId())
            .freePointAt(7000L)
            .cashPointAt(5000L)
            .build();
        when(pointUseRepository.findByMemberIdAndOrderNo(param.memberId(), param.orderNo())).thenReturn(pointuse);

        final var point1 = Point.builder()
            .pointNo(1L)
            .memberId(param.memberId())
            .pointType(PointTypes.ORDER_CONFIRM.getPointType())
            .remainAt(0L)
            .isCash(false)
            .build();

        final var point3 = Point.builder()
            .pointNo(3L)
            .originPointNo(2L)
            .memberId(param.memberId())
            .pointType("CASH_POINT")
            .remainAt(3500L)
            .isCash(true)
            .build();

        final var pointUseDetail1 = PointUseDetail.builder()
            .pointUseNo(pointuse.getPointUseNo())
            .point(point1)
            .pointAt(7000L)
            .build();

        final var pointUseDetail2 = PointUseDetail.builder()
            .pointUseNo(pointuse.getPointUseNo())
            .point(point3)
            .pointAt(3000L)
            .build();

        final var pointUseDetailList = List.of(pointUseDetail1, pointUseDetail2);
        when(pointUseDetailRepository.findCancelableDetails(pointuse.getPointUseNo())).thenReturn(pointUseDetailList);

        when(pointRepository.save(any())).thenReturn(point1);

        final var exception = assertThrows(PointApiException.class, () -> pointUseService.cancelUsedPoint(param));
        assertEquals(ErrorType.NOT_FOUND_SAVE_POINT, exception.getErrorType());
    }

    @DisplayName("포인트 사용 취소")
    @Test
    void test_cancelUsedPoint() {
        final var param = PointUseParam.builder()
            .memberId(12345L)
            .orderNo(10L)
            .useAt(10000L)
            .build();

        final var pointuse = PointUse.builder()
            .pointUseNo(1L)
            .memberId(param.memberId())
            .freePointAt(7000L)
            .cashPointAt(5000L)
            .build();
        when(pointUseRepository.findByMemberIdAndOrderNo(param.memberId(), param.orderNo())).thenReturn(pointuse);

        final var point1 = Point.builder()
            .pointNo(1L)
            .memberId(param.memberId())
            .pointType(PointTypes.ORDER_CONFIRM.getPointType())
            .remainAt(0L)
            .isCash(false)
            .build();

        final var point2 = Point.builder()
            .pointNo(2L)
            .memberId(param.memberId())
            .pointType("CASH_POINT")
            .remainAt(0L)
            .isCash(true)
            .build();

        final var point3 = Point.builder()
            .pointNo(3L)
            .originPointNo(2L)
            .memberId(param.memberId())
            .pointType("CASH_POINT")
            .remainAt(0L)
            .isCash(true)
            .build();

        final var pointUseDetail1 = PointUseDetail.builder()
            .pointUseNo(pointuse.getPointUseNo())
            .point(point1)
            .pointAt(7000L)
            .build();

        final var pointUseDetail2 = PointUseDetail.builder()
            .pointUseNo(pointuse.getPointUseNo())
            .point(point3)
            .pointAt(5000L)
            .build();

        final var pointUseDetailList = List.of(pointUseDetail1, pointUseDetail2);
        when(pointUseDetailRepository.findCancelableDetails(pointuse.getPointUseNo())).thenReturn(pointUseDetailList);

        when(pointRepository.save(any())).thenReturn(point1);
        when(pointRepository.findById(2L)).thenReturn(Optional.of(point2));

        pointUseService.cancelUsedPoint(param);

        verify(pointRepository, times(1)).findById(2L);
        verify(pointRepository, times(2)).save(any());
        verify(pointHistoryRepository, times(2)).save(any());
        verify(pointBalanceService, times(1)).plusPointBalance(param.memberId(), 7000L, 3000L);
    }
}
