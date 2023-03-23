package com.musinsa.point.api.exception;

import com.musinsa.point.api.model.type.OrderStatus;
import lombok.Getter;

@Getter
public class NotOrderCompleteException extends RuntimeException {

    private final OrderStatus status;

    public NotOrderCompleteException(OrderStatus status) {
        this.status = status;
    }
}
