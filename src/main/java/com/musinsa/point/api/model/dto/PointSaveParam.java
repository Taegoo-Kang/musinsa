package com.musinsa.point.api.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(name = "포인트 적립 요청")
public record PointSaveParam(
    @Schema(description = "회원 ID") @NotNull Long memberId
    , @Schema(description = "포인트 타입") @NotBlank String pointType
    , @Schema(description = "적립 포인트 설명") String description
    , @Schema(description = "적용 금액") @Min(1) long serviceAt
    , @Schema(description = "주문 번호") Long orderNo
) {}
