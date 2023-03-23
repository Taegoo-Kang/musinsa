package com.musinsa.point.api.exception.handler;

import com.musinsa.point.api.exception.NotOrderCompleteException;
import com.musinsa.point.api.exception.PointApiException;
import com.musinsa.point.api.model.dto.ErrorResponse;
import com.musinsa.point.api.model.type.ErrorType;
import java.security.InvalidParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParameterException(InvalidParameterException e) {

        final var errorResponse = ErrorResponse.builder()
            .httpStatus(HttpStatus.BAD_REQUEST.value())
            .errorMessage(e.getMessage())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PointApiException.class)
    public ResponseEntity<ErrorResponse> handlePointApiException(PointApiException e) {

        final var errorResponse = ErrorResponse.builder()
            .httpStatus(HttpStatus.BAD_REQUEST.value())
            .errorCode(e.getErrorType().errorCode())
            .errorMessage(e.getErrorType().errorMessage())
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotOrderCompleteException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyOrderStatusException(NotOrderCompleteException e) {

        final var errorMessage = switch (e.getStatus()) {
            case ORDER_CONFIRM -> "이미 구매확정 되었습니다.";
            case ORDER_CANCEL -> "취소한 상품은 구매확정 할 수 없습니다.";
            default -> "구매확정 할 수 없는 주문입니다.";
        };

        final var errorResponse = ErrorResponse.builder()
            .httpStatus(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .errorMessage(errorMessage)
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("{}", e.getMessage(), e);

        final var message = StringUtils.hasText(e.getMessage()) ? e.getMessage() : ErrorType.INTERNAL_SERVER_ERROR.errorMessage();
        final var errorResponse = ErrorResponse.builder()
            .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .errorCode(ErrorType.INTERNAL_SERVER_ERROR.errorCode())
            .errorMessage(message)
            .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
