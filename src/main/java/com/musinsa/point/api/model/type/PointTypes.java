package com.musinsa.point.api.model.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/*
 * 기본 포인트 타입
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PointTypes {

    ORDER_CONFIRM("PT_0001");

    private final String pointType;
}
