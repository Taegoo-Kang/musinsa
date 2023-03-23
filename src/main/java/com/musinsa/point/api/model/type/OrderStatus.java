package com.musinsa.point.api.model.type;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    ORDER_COMPLETE("P1", "구매 완료")
    , ORDER_CONFIRM("P2", "구매 확정")
    , ORDER_CANCEL("P9", "구매 취소")
    ;

    private final String code;
    private final String description;

    private static final Map<String, OrderStatus> ORDER_STATUS_MAP = Arrays.stream(OrderStatus.values())
        .collect(Collectors.toMap(OrderStatus::getCode, Function.identity()));

    public static OrderStatus of(final String code) {
        return ORDER_STATUS_MAP.get(code);
    }
}
