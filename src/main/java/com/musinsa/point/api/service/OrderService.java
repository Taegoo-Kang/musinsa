package com.musinsa.point.api.service;

import com.musinsa.point.api.domain.entity.OrderInfo;
import com.musinsa.point.api.domain.repository.OrderInfoRepository;
import com.musinsa.point.api.exception.NotOrderCompleteException;
import com.musinsa.point.api.model.dto.OrderDto;
import com.musinsa.point.api.model.dto.PointSaveParam;
import com.musinsa.point.api.model.dto.mapper.OrderMapper;
import com.musinsa.point.api.model.type.OrderStatus;
import com.musinsa.point.api.model.type.PointTypes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderInfoRepository orderInfoRepository;
    private final PointSaveService pointSaveService;

    /**
     * 구매 이력 조회
     *
     * @param memberId - 회원 ID
     * @return list
     */
    public List<OrderDto> findOrderLog(final Long memberId) {
        return orderInfoRepository.findByMemberIdOrderByOrderNoDesc(memberId).stream()
            .map(OrderMapper::toDto)
            .toList();
    }

    /**
     * 구매 확정
     *
     * @param memberId - 회원 ID
     * @param orderNo - 주문 번호
     * @return Long
     */
    @Transactional(rollbackFor = Exception.class)
    public Long orderConfirm(final Long memberId, final Long orderNo) {

        // 주문 정보 조회
        OrderInfo orderInfo = orderInfoRepository.findByOrderNoAndMemberId(orderNo, memberId)
            .orElseThrow(() -> new NotFoundException("주문 정보를 찾을 수 없습니다."));

        // 주문 완료 상태가 아니면 에러
        if (!OrderStatus.ORDER_COMPLETE.equals(orderInfo.getStatus())) {
            throw new NotOrderCompleteException(orderInfo.getStatus());
        }

        // 주문 상태 업데이트
        orderInfo.setStatus(OrderStatus.ORDER_CONFIRM);

        // 구매 포인트 적립 (구매 금액이 0원일 경우 적립 X)
        if (orderInfo.getPaymentAt() == 0L) {
            return 0L;
        }
        final var point = pointSaveService.savePoint(PointSaveParam.builder()
            .memberId(memberId)
            .pointType(PointTypes.ORDER_CONFIRM.getPointType())
            .serviceAt(orderInfo.getPaymentAt())
            .build());

        return point.pointAt();
    }
}
