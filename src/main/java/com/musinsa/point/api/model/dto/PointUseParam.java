package com.musinsa.point.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(name = "포인트 사용 요청")
public record PointUseParam(
    @Schema(description = "회원 ID") @NotNull Long memberId
   , @Schema(description = "주문 번호") @NotNull Long orderNo
   , @Schema(description = "사용 금액") @Min(1) long useAt
) {}
