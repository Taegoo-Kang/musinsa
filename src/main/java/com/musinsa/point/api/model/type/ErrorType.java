package com.musinsa.point.api.model.type;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorType {

    INVALID_POINT_TYPE("PS01", "사용할 수 없는 포인트 타입입니다.")
    , INVALID_SAVE_AMOUNT("PS02", "적립 금액은 0보다 커야합니다.")
    , OVER_ENABLE_POINT("PU01", "가용 포인트를 초과했습니다.")
    , USE_EXPIRED_POINT("PU02", "유효기간이 지난 포인트는 사용 할 수 없습니다.")
    , NOT_FOUND_USED_POINT("PC01", "포인트 사용 정보를 찾을 수 없습니다.")
    , OVER_CANCEL_USED_POINT("PC02", "사용한 금액보다 큰 금액을 취소할 수 없습니다.")
    , NOT_FOUND_SAVE_POINT("PC03", "포인트 적립 정보를 찾을 수 없습니다.")
    , FAIL_CANCEL_USED_POINT("PC04", "이미 사용 취소된 포인트 입니다.")
    , INTERNAL_SERVER_ERROR("ER99", "알 수 없는 에러 발생. 잠시 후 다시 시도해주세요.")
    ;

    private final String errorCode;
    private final String errorMessage;

    public String errorCode() {
        return this.errorCode;
    }

    public String errorMessage() {
        return this.errorMessage;
    }
}
