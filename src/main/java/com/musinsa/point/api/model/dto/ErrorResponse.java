package com.musinsa.point.api.model.dto;

import lombok.Builder;

@Builder
public record ErrorResponse(
    int httpStatus
    , String errorCode
    , String errorMessage
    , String detail
) {}
