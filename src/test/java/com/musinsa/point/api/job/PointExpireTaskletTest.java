package com.musinsa.point.api.job;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musinsa.point.api.domain.entity.Point;
import com.musinsa.point.api.domain.repository.PointHistoryRepository;
import com.musinsa.point.api.domain.repository.PointRepository;
import com.musinsa.point.api.exception.PointApiException;
import com.musinsa.point.api.model.type.ErrorType;
import com.musinsa.point.api.service.PointBalanceService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class PointExpireTaskletTest {

    @Mock
    private PointRepository pointRepository;
    @Mock
    private PointHistoryRepository pointHistoryRepository;
    @Mock
    private PointBalanceService pointBalanceService;

    @InjectMocks
    private PointExpireTasklet pointExpireTasklet;

    @Test
    void test_notFoundOriginPoint() {
        final var point1 = Point.builder()
            .pointNo(1L)
            .remainAt(2000L)
            .build();

        final var point2 = Point.builder()
            .pointNo(2L)
            .originPointNo(1L)
            .remainAt(1000L)
            .build();

        when(pointRepository.findExpiredPoints()).thenReturn(List.of(point1, point2));

        final var exception = assertThrows(PointApiException.class, () -> pointExpireTasklet.execute(null,  null));
        assertEquals(ErrorType.NOT_FOUND_SAVE_POINT, exception.getErrorType());
    }

    @Test
    void test_pointExpire() throws Exception {

        final var point1 = Point.builder()
            .pointNo(1L)
            .memberId(12345L)
            .remainAt(2000L)
            .isCash(false)
            .build();

        final var point2 = Point.builder()
            .pointNo(2L)
            .memberId(54321L)
            .remainAt(1000L)
            .isCash(false)
            .build();

        final var point3 = Point.builder()
            .pointNo(3L)
            .memberId(54321L)
            .originPointNo(2L)
            .remainAt(500L)
            .isCash(false)
            .build();

        final var point4 = Point.builder()
            .pointNo(4L)
            .memberId(54321L)
            .originPointNo(2L)
            .remainAt(10L)
            .isCash(false)
            .build();

        final var point5 = Point.builder()
            .pointNo(5L)
            .memberId(12345L)
            .remainAt(5000L)
            .isCash(true)
            .build();

        when(pointRepository.findExpiredPoints()).thenReturn(List.of(point1, point2, point3, point4, point5));
        when(pointRepository.findById(2L)).thenReturn(Optional.of(point2));

        pointExpireTasklet.execute(null, null);
        verify(pointRepository, times(2)).findById(2L);
        verify(pointHistoryRepository, times(5)).save(any());
        verify(pointBalanceService, times(1)).minusPointBalance(point1.getMemberId(), 2000L, 0L);
        verify(pointBalanceService, times(1)).minusPointBalance(point2.getMemberId(), 1000L, 0L);
        verify(pointBalanceService, times(1)).minusPointBalance(point3.getMemberId(), 500L, 0L);
        verify(pointBalanceService, times(1)).minusPointBalance(point4.getMemberId(), 10L, 0L);
        verify(pointBalanceService, times(1)).minusPointBalance(point5.getMemberId(), 0L, 5000L);
    }
}
