package com.musinsa.point.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musinsa.point.api.domain.entity.Point;
import com.musinsa.point.api.domain.entity.PointType;
import com.musinsa.point.api.domain.repository.PointHistoryRepository;
import com.musinsa.point.api.domain.repository.PointRepository;
import com.musinsa.point.api.domain.repository.PointTypeRepository;
import com.musinsa.point.api.exception.PointApiException;
import com.musinsa.point.api.model.dto.PointSaveParam;
import com.musinsa.point.api.model.type.ErrorType;
import com.musinsa.point.api.model.type.PointTypes;
import com.musinsa.point.api.util.DateTimeUtils;
import com.musinsa.point.api.util.MathUtils;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StringUtils;

@ExtendWith(MockitoExtension.class)
class PointSaveServiceTest {

    @Mock
    private PointTypeRepository pointTypeRepository;
    @Mock
    private PointRepository pointRepository;
    @Mock
    private PointHistoryRepository pointHistoryRepository;
    @Mock
    private PointBalanceService pointBalanceService;

    @InjectMocks
    private PointSaveService pointSaveService;

    @DisplayName("0원 적립")
    @Test
    void test_invalidSaveAmount() {
        final var param = PointSaveParam.builder()
            .memberId(10003L)
            .pointType(PointTypes.ORDER_CONFIRM.getPointType())
            .serviceAt(0L)
            .build();

        final var exception = assertThrows(PointApiException.class, () -> pointSaveService.savePoint(param));
        assertEquals(ErrorType.INVALID_SAVE_AMOUNT, exception.getErrorType());
    }

    @DisplayName("유효하지 않은 포인트 타입")
    @Test
    void test_invalidPointType() {
        final var param = PointSaveParam.builder()
            .memberId(10003L)
            .pointType("TestPointType")
            .serviceAt(13726L)
            .build();

        final var exception = assertThrows(PointApiException.class, () -> pointSaveService.savePoint(param));
        assertEquals(ErrorType.INVALID_POINT_TYPE, exception.getErrorType());
    }

    @DisplayName("포인트 적립")
    @Test
    void test_savePoint() {
        final var param = PointSaveParam.builder()
            .memberId(10003L)
            .pointType(PointTypes.ORDER_CONFIRM.getPointType())
            .serviceAt(13726L)
            .build();

        final var pointType = PointType.builder()
            .pointType(PointTypes.ORDER_CONFIRM.getPointType())
            .pointTypeName("구매 적립")
            .expirePeriod(365)
            .rate(1f)
            .useYn("Y")
            .isCash(false)
            .build();

        final var pointAt = MathUtils.getPercentage(param.serviceAt(), pointType.getRate()).longValue();
        final var expireDate = LocalDate.now();
        final var point = Point.builder()
            .memberId(param.memberId())
            .pointType(pointType.getPointType())
            .description(pointType.getPointTypeName())
            .accumulateAt(pointAt)
            .remainAt(pointAt)
            .isCash(pointType.getIsCash())
            .expireDate(expireDate)
            .build();

        when(pointTypeRepository.findEnabledByPointType(any())).thenReturn(pointType);
        when(pointRepository.save(any())).thenReturn(point);

        final var response = pointSaveService.savePoint(param);

        verify(pointHistoryRepository, times(1)).save(any());
        verify(pointBalanceService, times(1)).plusPointBalance(anyLong(), anyLong(), anyLong());

        final var expectedDescription = StringUtils.hasText(param.description()) ? param.description() : pointType.getPointTypeName();
        assertEquals(param.memberId(), response.memberId());
        assertEquals(expectedDescription, response.description());
        assertEquals(pointAt, response.pointAt());
        assertEquals(expireDate.format(DateTimeUtils.POINT_DATE_FORMAT), response.expireDate());
    }
}
