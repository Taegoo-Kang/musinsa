package com.musinsa.point.api.exception;

import com.musinsa.point.api.model.type.ErrorType;
import lombok.Getter;

@Getter
public class PointApiException extends RuntimeException {

    private final ErrorType errorType;

    public PointApiException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public PointApiException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }
}
