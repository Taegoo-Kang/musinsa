package com.musinsa.point.api.model.type;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointStatus {

    SAVE("S", "적립")
    , EXPIRE("X", "만료")
    , USE("U", "사용")
    , CANCEL("C", "취소")
    ;

    private final String code;
    private final String description;

    private static final Map<String, PointStatus> POINT_STATUS_MAP = Arrays.stream(PointStatus.values())
        .collect(Collectors.toMap(PointStatus::getCode, Function.identity()));

    public static PointStatus of(final String code) {
        return POINT_STATUS_MAP.get(code);
    }
}
