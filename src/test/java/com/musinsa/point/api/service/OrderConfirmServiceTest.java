package com.musinsa.point.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musinsa.point.api.domain.entity.OrderInfo;
import com.musinsa.point.api.domain.entity.Point;
import com.musinsa.point.api.domain.repository.OrderInfoRepository;
import com.musinsa.point.api.exception.NotOrderCompleteException;
import com.musinsa.point.api.model.dto.PointDto;
import com.musinsa.point.api.model.type.OrderStatus;
import com.musinsa.point.api.util.DateTimeUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webjars.NotFoundException;

@ExtendWith(MockitoExtension.class)
class OrderConfirmServiceTest {

    @Mock
    private OrderInfoRepository orderInfoRepository;
    @Mock
    private PointSaveService pointSaveService;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("구매 목록 조회")
    @Test
    void test_orderLog() {
        final var memberId = 12345L;

        final var order1 = OrderInfo.builder()
            .orderNo(1L)
            .memberId(memberId)
            .paymentAt(15000L)
            .status(OrderStatus.ORDER_CONFIRM)
            .orderDate(LocalDate.now())
            .build();

        final var order2 = OrderInfo.builder()
            .orderNo(2L)
            .memberId(memberId)
            .paymentAt(0L)
            .status(OrderStatus.ORDER_COMPLETE)
            .orderDate(LocalDate.now())
            .build();

        final var order3 = OrderInfo.builder()
            .orderNo(3L)
            .memberId(memberId)
            .paymentAt(9999L)
            .status(OrderStatus.ORDER_CANCEL)
            .orderDate(LocalDate.now())
            .build();

        when(orderInfoRepository.findByMemberIdOrderByOrderNoDesc(memberId)).thenReturn(List.of(order3, order2, order1));

        final var orderLogs = orderService.findOrderLog(memberId);
        assertEquals(3, orderLogs.size());
        assertEquals(OrderStatus.ORDER_CANCEL.getDescription(), orderLogs.get(0).status());
        assertEquals(order2.getPaymentAt(), orderLogs.get(1).paymentAt());
        assertEquals(order2.getOrderDate().format(DateTimeUtils.POINT_DATE_FORMAT), orderLogs.get(1).orderDate());
        assertEquals(OrderStatus.ORDER_CONFIRM.getDescription(), orderLogs.get(2).status());
    }

    @DisplayName("주문 조회 실패")
    @Test
    void test_notFoundOrder() {
        final var exception = assertThrows(NotFoundException.class, () -> orderService.orderConfirm(0L, 0L));
        assertEquals("주문 정보를 찾을 수 없습니다.", exception.getMessage());
    }

    @DisplayName("구매 확정 실패")
    @Test
    void test_failOrderConfirm() {
        final var order = OrderInfo.builder()
            .orderNo(1L)
            .memberId(12345L)
            .paymentAt(10000L)
            .status(OrderStatus.ORDER_CONFIRM)
            .orderDate(LocalDate.now())
            .build();

        when(orderInfoRepository.findByOrderNoAndMemberId(order.getOrderNo(), order.getMemberId())).thenReturn(Optional.of(order));

        assertThrows(NotOrderCompleteException.class, () -> orderService.orderConfirm(order.getMemberId(), order.getOrderNo()));
    }

    @DisplayName("0원 구매 확정")
    @Test
    void test_zeroPayment() {
        final var order = OrderInfo.builder()
            .orderNo(1L)
            .memberId(12345L)
            .paymentAt(0L)
            .status(OrderStatus.ORDER_COMPLETE)
            .orderDate(LocalDate.now())
            .build();

        when(orderInfoRepository.findByOrderNoAndMemberId(order.getOrderNo(), order.getMemberId())).thenReturn(Optional.of(order));

        orderService.orderConfirm(order.getMemberId(), order.getOrderNo());
        verify(pointSaveService, times(0)).savePoint(any());
    }

    @DisplayName("구매 확정")
    @Test
    void test_orderConfirm() {

        final var order = OrderInfo.builder()
            .orderNo(1L)
            .memberId(12345L)
            .paymentAt(10000L)
            .status(OrderStatus.ORDER_COMPLETE)
            .orderDate(LocalDate.now())
            .build();
        when(orderInfoRepository.findByOrderNoAndMemberId(order.getOrderNo(), order.getMemberId())).thenReturn(Optional.of(order));

        final var pointDto = PointDto.builder()
            .pointAt(100L)
            .build();
        when(pointSaveService.savePoint(any())).thenReturn(pointDto);

        final var pointAt = orderService.orderConfirm(order.getMemberId(), order.getOrderNo());
        assertEquals(pointDto.pointAt(), pointAt);
    }
}
