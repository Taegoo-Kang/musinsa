package com.musinsa.point.api.model.dto.mapper;

import com.musinsa.point.api.domain.entity.OrderInfo;
import com.musinsa.point.api.model.dto.OrderDto;
import com.musinsa.point.api.util.DateTimeUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class OrderMapper {

    public static OrderDto toDto(final OrderInfo entity) {
        if (entity == null) {
            return null;
        }

        return OrderDto.builder()
            .orderNo(entity.getOrderNo())
            .memberId(entity.getMemberId())
            .paymentAt(entity.getPaymentAt())
            .status(entity.getStatus().getDescription())
            .orderDate(entity.getOrderDate().format(DateTimeUtils.POINT_DATE_FORMAT))
            .build();
    }
}
