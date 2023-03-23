package com.musinsa.point.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "주문 정보")
public record OrderDto(
    @Schema(description = "주문 번호") long orderNo
    , @Schema(description = "회원 ID") long memberId
    , @Schema(description = "결제 금액") long paymentAt
    , @Schema(description = "구매 상태") String status
    , @Schema(description = "구매 일자") String orderDate
) {}
